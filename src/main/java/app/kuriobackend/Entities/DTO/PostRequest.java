package app.kuriobackend.Entities.DTO;

import app.kuriobackend.Entities.Model.User;

import java.util.ArrayList;

public record PostRequest(String titulo, String descripcion, ArrayList<String> imagenes, User user, String oid, String licencia) {}
