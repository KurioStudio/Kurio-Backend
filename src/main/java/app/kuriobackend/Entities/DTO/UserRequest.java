package app.kuriobackend.Entities.DTO;

import app.kuriobackend.Entities.Model.User;

public record UserRequest(String username, String email, String password) {

}
