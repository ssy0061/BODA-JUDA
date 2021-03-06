package com.aeye.thirdeye.api;

import com.aeye.thirdeye.dto.LeaderBoardDto;
import com.aeye.thirdeye.dto.request.ChangePasswordRequest;
import com.aeye.thirdeye.dto.request.LoginRequest;
import com.aeye.thirdeye.dto.response.ApiResponse;
import com.aeye.thirdeye.dto.response.ErrorResponse;
import com.aeye.thirdeye.dto.response.ProfileResponseDto;
import com.aeye.thirdeye.entity.User;
import com.aeye.thirdeye.entity.auth.ProviderType;
import com.aeye.thirdeye.entity.auth.RoleType;
import com.aeye.thirdeye.repository.UserRepository;
import com.aeye.thirdeye.service.FileService;
import com.aeye.thirdeye.service.UserService;
import com.aeye.thirdeye.service.auth.CustomGoogleUserService;
import com.aeye.thirdeye.token.JwtTokenProvider;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "?????? ?????? API")
public class UserApiController {

    private final UserRepository userRepository;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final MessageSource messageSource;

    private final JwtTokenProvider jwtTokenProvider;

    private final CustomGoogleUserService customGoogleUserService;

    private final FileService fileService;

    /**
     * ?????? ??????
     * @param request
     * @param bindingResult
     * @return
     * ?????? ?????? : 201/ 404
     */
    @PostMapping("/accounts/signup")
    @ApiOperation(value = "????????????", notes = "")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 201, message = "???????????? ??????"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "validation ??????")
    })
    public ResponseEntity<?> signup(@Validated @RequestBody CreateUserRequest request,
                                 BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(bindingResult.getAllErrors().get(0).getDefaultMessage()));
        }

        System.out.println(request.getEmail());
        System.out.println(request.getPassword());
        System.out.println(request.getNickname());
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setNickName(request.getNickname());
        user.setProviderType(ProviderType.LOCAL);
        user.setCreatedAt(LocalDateTime.now());
        user.setModifiedAt(LocalDateTime.now());
        user.setUserId(request.getUserId());
        user.setRoleType(RoleType.USER);
        user.setProfileImage("/default/default_profile.png");

        try {
            Long id = userService.join(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        }
        catch (IllegalStateException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource
                            .getMessage("error.same.id", null, LocaleContextHolder.getLocale())));
        }
    }

    @Data
    static class CreateUserRequest {
        // email validation ??????
        @Email(message = "{error.format.email}", regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
        @NotNull(message = "{email.notempty}")
        private String email;
        @Size(min=2, max=20, message = "{error.size.nickName}")
        private String nickname;
        // password validation ??????
        @Size(min=8, max=14, message = "{error.size.password}")
//        @Length(min=3, max=128, message = "???????????? ?????? ?????????")
        private String password;
        @Size(min=2, max=20, message = "{error.size.userId}") String userId;
//        @Size(max = 512) String profileImage;
//        ProviderType providerType;
//        RoleType roleType;
//        LocalDateTime createdAt;
//        LocalDateTime modifiedAt;
    }

    /**
     * ?????? ??????
     * @param token
     * @return
     * ?????? ?????? : 200 / 401
     */
    @DeleteMapping("/accounts/signout")
    @ApiOperation(value = "????????????", notes = "")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 200, message = "?????? ?????? ??????"),
            @io.swagger.annotations.ApiResponse(code = 401, message = "token ?????? ??????")
    })
    public ResponseEntity<?> signout(@ApiParam(value = "jwt ??????")@RequestHeader(value = "Authorization") String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long id = jwtTokenProvider.getId(token);

        userService.deleteUser(id);

        SecurityContextHolder.clearContext();
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /**
     * ????????? JWT ??????
     * @param loginRequest // {id , password}
     * @return
     * ?????? ?????? : 200 / 401 / 404
     */
    @PostMapping("/accounts/login")
    @ApiOperation(value = "?????? ?????????", notes = "")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 200, message = "????????? ??????", response = LoginUserResponse.class),
            @io.swagger.annotations.ApiResponse(code = 401, message = "???????????? ?????????"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "?????? ?????? ??????")
    })
    public ResponseEntity<?> login(@ApiParam(value = "id, Password")@RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByUserId(loginRequest.getUserId());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(messageSource.getMessage("error.none.user", null, LocaleContextHolder.getLocale())));
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(messageSource.getMessage("error.wrong.password", null, LocaleContextHolder.getLocale())));
        }

        String token = jwtTokenProvider.createToken(user.getNickName(), user.getId());

        return ResponseEntity.ok(new LoginUserResponse(token));
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public class LoginUserResponse {
        private String token;
        public LoginUserResponse(String accessToken) {
            this.token = accessToken;
        }
    }

    /**
     * ????????? ?????? API
     * @param id
     * @return
     * ?????? ?????? : 200 / 404
     */
    @GetMapping("/accounts/info/{id}")
    @ApiOperation(value = "????????? ??????", notes = "")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 200, message = "", response = ProfileResponseDto.class),
            @io.swagger.annotations.ApiResponse(code = 404, message = "?????? ?????? ??????")
    })
    public ResponseEntity<?> getProfile(@ApiParam(value = "user ????????? ???") @PathVariable("id") Long id){
        ProfileResponseDto profileResponseDto = userService.getProfile(id);
        if(profileResponseDto == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(profileResponseDto);
    }

    /**
     * ???????????? ?????? API
     * @param pageable
     * @return
     * ?????? ?????? : 200 / 404
     */
    @GetMapping("/accounts/rank")
    @ApiOperation(value = "???????????? ?????? API", notes = "???????????? page, size ?????? (default page = 0, size = 20)")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 200, message = "?????? ??????", response = LeaderBoardDto.class, responseContainer = "List"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "?????? ?????? ??????")
    })
    public ResponseEntity<?> getLeaderBoard(@PageableDefault(size=20, sort="ranking", direction = Sort.Direction.ASC) Pageable pageable){
        List<LeaderBoardDto> leaderBoardDtos = userService.getLeaderBoard(pageable);
        if(leaderBoardDtos == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(leaderBoardDtos);
    }


    /**
     * ?????? ????????? API
     * @param token
     * @return
     * ?????? ?????? : 200 / 401
     */
    @PostMapping("/accounts/auth/login")
    @ApiOperation(value = "?????? ?????????", notes = "")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 200, message = "????????? ??????", response = LoginUserResponse.class),
            @io.swagger.annotations.ApiResponse(code = 401, message = "???????????? ?????????")
    })
    public ApiResponse<?> googleLogin(@ApiParam(value = "?????? ??????")
                                       @RequestHeader(value = "Authorization") String token) {
        GoogleIdToken.Payload payload = null;
        String tokenResult = "";
        try {
            payload = userService.googleLogin(token);

            // DB??? ?????? ??????
            User user = customGoogleUserService.loadUser(payload);

            // Token ??????
            tokenResult = jwtTokenProvider.createToken(user.getNickName(), user.getId());

            // Token return
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
            return ApiResponse.invalidAccessToken();
        }

        return ApiResponse.success("token", tokenResult);
    }

    /**
     * ???????????? ?????? API
     * @param token
     * @param changePasswordRequest // {paswword, passwordConfirm}
     * @return
     * ?????? ?????? : 200 / 401 / 406
     */
    @PutMapping("/accounts/update/password")
    @ApiOperation(value = "???????????? ??????", notes = "")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 200, message = "?????? ??????"),
            @io.swagger.annotations.ApiResponse(code = 401, message = "?????? ?????? ??????"),
            @io.swagger.annotations.ApiResponse(code = 406, message = "???????????? validation ??????")
    })
    public ResponseEntity<?> changePassword(@ApiParam(value = "Jwt ??????")
                                                @RequestHeader(value = "Authorization") String token,
                                            @ApiParam(value = "Password") @RequestBody ChangePasswordRequest changePasswordRequest) {
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        else{
            Long id = jwtTokenProvider.getId(token);
            if(changePasswordRequest == null ||
                    !changePasswordRequest.getPassword().trim().equals(changePasswordRequest.getPasswordConfirm().trim())){
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Password Confirm is not Accepted");
            }
            userService.updatePassword(id, changePasswordRequest.getPassword());
        }

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }


    /**
     * ?????? ?????? ??????
     * @param token
     * @param file
     * @param nickName
     * @param email
     * @return
     * ?????? ?????? 200 / 400 / 401
     */
    @PutMapping("/accounts/update/userinfo")
    @ApiOperation(value = "?????? ?????? ??????", notes = "")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 200, message = "?????? ??????"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "validation ??????"),
            @io.swagger.annotations.ApiResponse(code = 401, message = "?????? ?????? ??????")
    })
    public ResponseEntity<?> changeUserInfo(@ApiParam(value = "jwt ??????")@RequestHeader(value = "Authorization") String token,
                                            @RequestPart(value = "image", required = false) MultipartFile file,
                                            @RequestPart(value = "nickName", required = false) String nickName,
                                            @RequestPart(value = "email", required = false) String email) {
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }


        Long id = jwtTokenProvider.getId(token);
        String image = userService.getProfileImgUrl(id);
        if(nickName == null || email == null || file == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Content is null");
        }

        try {
            image = fileService.imageUploadGCS(file, id);
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource
                            .getMessage("error.wrong", null, LocaleContextHolder.getLocale())));
        }

        userService.updateUserInfo(id, nickName, email, image);



        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

        @PutMapping("/test/test")
        public ResponseEntity<?> zzz() {

            userService.testDeleteLeader();
            userService.testLeader();

            return null;
        }



    }
