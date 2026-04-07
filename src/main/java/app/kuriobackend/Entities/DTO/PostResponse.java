package app.kuriobackend.Entities.DTO;

import app.kuriobackend.Entities.Model.User;

import java.util.ArrayList;

public record PostResponse(String id, String titulo, String descripcion, ArrayList<String> imagenes, ArrayList<String> likedBy, Integer cantComentarios, User user, String oid, String licencia) {}
