package com.example.demo.src.like;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.like.model.*;
import com.example.demo.src.order.OrderProvider;
import com.example.demo.src.order.OrderService;
import com.example.demo.src.order.model.GetOrderRes;
import com.example.demo.src.order.model.PostOrderReq;
import com.example.demo.src.order.model.PostOrderRes;
import com.example.demo.src.user.model.PatchProfileReq;
import com.example.demo.src.user.model.User;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@RestController
@RequestMapping("/app/users")
public class LikeController {

    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log를 남기기: 일단은 모르고 넘어가셔도 무방합니다.

    @Autowired
    private final LikeProvider likeProvider;
    @Autowired
    private final LikeService likeService;
    @Autowired
    private final JwtService jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!


    public LikeController(LikeProvider likeProvider, LikeService likeService, JwtService jwtService) {
        this.likeProvider = likeProvider;
        this.likeService = likeService;
        this.jwtService = jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!
    }

    // ******************************************************************************

    //Query String
    @ResponseBody
    @GetMapping("/{userIdx}/likes") // (GET) 127.0.0.1:9000/app/:userIdx/likes
    // GET 방식의 요청을 매핑하기 위한 어노테이션
    public BaseResponse<List<GetLikeRes>> getLikes(@PathVariable("userIdx") int userIdx) {
        try {
            List<GetLikeRes> getLikesRes;
            getLikesRes = likeProvider.getLikesByUserIdx(userIdx);
            return new BaseResponse<>(getLikesRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PostMapping("/{userIdx}/likes")
    public BaseResponse<PostLikeRes> Like(@PathVariable("userIdx") int userIdx, @RequestBody PostLikeReq postLikeReq) {
        try {
            PostLikeRes postLikeRes = likeProvider.Like(postLikeReq);
            return new BaseResponse<>(postLikeRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PatchMapping("/{userIdx}/likes")
    public BaseResponse<String> modifyLike(@PathVariable("userIdx") int userIdx, @RequestBody Like like) {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            PatchLikeReq patchLikeReq = new PatchLikeReq(userIdx, like.getStoreIdx(), like.getStatus());
            likeService.modifyLike(patchLikeReq);

            String result = "찜 목록에서 삭제되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
