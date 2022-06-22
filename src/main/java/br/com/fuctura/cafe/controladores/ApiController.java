package br.com.fuctura.cafe.controladores;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import br.com.fuctura.cafe.config.SwaggerConfig;
import br.com.fuctura.cafe.entidades.dtos.MensagemDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = {SwaggerConfig.API_TAG})
@RestController
@RequestMapping("/")
public class ApiController {
	
	@ApiIgnore
	@GetMapping()
	public RedirectView redirectSwagger() {
		return new RedirectView("/swagger-ui.html");		
	}
	
	@ApiOperation(value="Verifica disponibilidade do serviço api do sistema Café")
	@GetMapping(value="api", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<MensagemDto> index() {
		
		MensagemDto dto = new MensagemDto(HttpStatus.OK.value(), "Api do Sistema Café está online!");
		dto.setCaminho("/api");
		
		return ResponseEntity.status(HttpStatus.OK).body(dto);
	}
}
