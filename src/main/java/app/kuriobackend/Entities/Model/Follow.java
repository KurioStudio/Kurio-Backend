package app.kuriobackend.Entities.Model;

import app.kuriobackend.Entities.DTO.FollowRequest;
import app.kuriobackend.Entities.DTO.FollowResponse;
import com.google.firebase.database.annotations.NotNull;

import java.time.LocalDate;

public record Follow(@NotNull String idFollower, @NotNull String idFollowed, String createdAt) {
    public static Follow fromRequest(FollowRequest rq){
        return new Follow(
            rq.idFollower(),
            rq.idFollowed(),
            LocalDate.now().toString()
        );
    }

    public FollowResponse toResponse() {
        return new FollowResponse(
            this.idFollower,
            this.idFollowed,
            this.createdAt
        );
    }
}
