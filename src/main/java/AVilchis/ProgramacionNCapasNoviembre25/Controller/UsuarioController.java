package AVilchis.ProgramacionNCapasNoviembre25.Controller;

import AVilchis.ProgramacionNCapasNoviembre25.ML.Direccion;
import AVilchis.ProgramacionNCapasNoviembre25.ML.Pais;
import AVilchis.ProgramacionNCapasNoviembre25.ML.Result;
import AVilchis.ProgramacionNCapasNoviembre25.ML.Rol;
import AVilchis.ProgramacionNCapasNoviembre25.ML.Usuario;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("usuario")
public class UsuarioController {

    private static final String urlBase = "http://localhost:8080/api";

    @GetMapping
    public String GetAll(Model model) {

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Result<List<Usuario>>> responseEntity
                = restTemplate.exchange(
                        urlBase + "/usuario",
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<Result<List<Usuario>>>() {
                }
                );
        if (responseEntity.getStatusCode().value() == 200) {
            Result<List<Usuario>> result = responseEntity.getBody();
            model.addAttribute("usuarios", result.Object);
            model.addAttribute("usuarioBusqueda", new Usuario());

        } else if (responseEntity.getStatusCode().value() == 500) {
            // manejar error
        }
        return "UsuarioIndex";
    }

    @GetMapping("/form")
    public String Form(Model model) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Result<List<Rol>>> responseEntityRol
                = restTemplate.exchange(urlBase + "/rol", HttpMethod.GET,
                        HttpEntity.EMPTY, new ParameterizedTypeReference<Result<List<Rol>>>() {
                });
        Result resultRol = responseEntityRol.getBody();
        model.addAttribute("rol", resultRol.Object);

        ResponseEntity<Result<List<Pais>>> responseEntityPais
                = restTemplate.exchange(urlBase + "/pais", HttpMethod.GET,
                        HttpEntity.EMPTY, new ParameterizedTypeReference<Result<List<Pais>>>() {
                });
        Result resultPais = responseEntityPais.getBody();
        model.addAttribute("pais", resultPais.Object);
        Usuario usuario = new Usuario();
        usuario.Direcciones = new ArrayList<>();
        usuario.Direcciones.add(new Direccion());
        model.addAttribute("usuario", usuario);
        return "UsuarioForm";
    }

    @PostMapping("/add")
    public String AddUser(@ModelAttribute("usuario") Usuario usuario) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // Llamada POST al API para agregar usuario
            HttpEntity<Usuario> request = new HttpEntity<>(usuario);
            ResponseEntity<Result> response = restTemplate.exchange(
                    urlBase + "/agregar", // Endpoint de tu API que maneja POST
                    HttpMethod.POST,
                    request,
                    Result.class
            );

        } catch (Exception e) {

        }
        return "redirect:/usuario";
    }

}
