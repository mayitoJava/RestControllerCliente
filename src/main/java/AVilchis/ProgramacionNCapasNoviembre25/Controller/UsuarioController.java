package AVilchis.ProgramacionNCapasNoviembre25.Controller;

import AVilchis.ProgramacionNCapasNoviembre25.ML.Colonia;
import AVilchis.ProgramacionNCapasNoviembre25.ML.Direccion;
import AVilchis.ProgramacionNCapasNoviembre25.ML.Estado;
import AVilchis.ProgramacionNCapasNoviembre25.ML.Municipio;
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
import org.springframework.web.bind.annotation.PathVariable;
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
    
//     @PostMapping("/add")
//    public String addUsuario(@Valid @ModelAttribute("Usuario") Usuario usuario,BindingResult bindingResult,
//            @RequestParam("imagenFile") MultipartFile imagenFile, Model model) {
//
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("Usuario", usuario);
//            return "/FormUsu"; // Vuelve al formulario si hay errores
//        }
//
//        
//        RestTemplate restTemplate = new RestTemplate();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//        
//        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//
//        // Parte 1: El objeto Usuario 
//        HttpHeaders jsonHeaders = new HttpHeaders();
//        jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<Usuario> usuarioPart = new HttpEntity<>(usuario, jsonHeaders);
//        body.add("usuario", usuarioPart);
//
//        // Parte 2: El archivo (si existe)
//        if (imagenFile != null && !imagenFile.isEmpty()) {
//            body.add("Imagen", imagenFile.getResource()); 
//        }
//
//        // 3. Crear la petición completa
//        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
//
//        try {
//            ResponseEntity<Result> response = restTemplate.exchange(
//                    urlBase + "usuario/add",
//                    HttpMethod.POST,
//                    requestEntity,
//                    Result.class
//            );
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return "redirect:/usuario";
//    }

    @GetMapping("detail/{IdUsuario}")
    public String Detail(@PathVariable("IdUsuario") int IdUsuario, Model model) {
        
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Result<Usuario>> responseEntity
                = restTemplate.exchange(urlBase + "/usuario/" + IdUsuario, HttpMethod.GET,
                        HttpEntity.EMPTY, new ParameterizedTypeReference<Result<Usuario>>() {
                });

         ResponseEntity<Result<List<Rol>>> responseEntityRol
                = restTemplate.exchange(urlBase + "/rol", HttpMethod.GET,
                        HttpEntity.EMPTY, new ParameterizedTypeReference<Result<List<Rol>>>() {
                });

        Result resultRol = responseEntityRol.getBody();
        model.addAttribute("Roles", resultRol.Object);

        ResponseEntity<Result<List<Pais>>> responseEntityPais
                = restTemplate.exchange(urlBase + "/pais", HttpMethod.GET,
                        HttpEntity.EMPTY, new ParameterizedTypeReference<Result<List<Pais>>>() {
                });

        Result resultPais = responseEntityPais.getBody();
        
        if (responseEntity.getStatusCode().value() == 200) {
            Result result = responseEntity.getBody();

            model.addAttribute("Roles", resultRol.Object);

            model.addAttribute("Paises", resultPais.Object);

            model.addAttribute("usuario", result.Object);
            
            model.addAttribute("Direccion", new Direccion());
        }

        return "UsuarioEditar";
    }
    

}
