package com.itti.digital.atm.atm_authorization_api.controller;


import com.itti.digital.atm.atm_authorization_api.AtmAuthorizationApiApplication;
import com.itti.digital.atm.atm_authorization_api.errors.AlfaMsException;
import com.itti.digital.atm.atm_authorization_api.errors.ErrorBody;
import com.itti.digital.atm.atm_authorization_api.models.authentication.RequestGetWebToken;
import com.itti.digital.atm.atm_authorization_api.models.authentication.ResponseGetWebToken;
import com.itti.digital.atm.atm_authorization_api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;

/**
 * @author Giancarlo Migliore
 */
@CrossOrigin(origins = {"*"})
@Tag(name = "auth")
@RestController
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @Operation(
            description = "Obtener token JWT",
            operationId = "getWebToken",
            responses = {
                    @ApiResponse(
                            description = AtmAuthorizationApiApplication.SUCCESS,
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = AtmAuthorizationApiApplication.NO_CONTENT,
                            responseCode = "204",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            description = AtmAuthorizationApiApplication.PARAM_INVALID,
                            responseCode = "400",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            description = AtmAuthorizationApiApplication.BUSINESS_ENTITY_FAILURE,
                            responseCode = "422",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorBody.class)
                            )
                    ),
                    @ApiResponse(
                            description = AtmAuthorizationApiApplication.INTERNAL_SERVER_ERROR,
                            responseCode = "500",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorBody.class)
                            )
                    )
            }
    )
    @Parameters({
            @Parameter(in = ParameterIn.HEADER, name = "Crypto-Enabled", schema = @Schema(type = "string", allowableValues = {"true", "false"}, defaultValue = "true")),
            @Parameter(in = ParameterIn.HEADER, name = "Ted-Id", schema = @Schema(type = "string"),allowEmptyValue = true,required = false)
    })
    @PostMapping("/v1/getWebToken")
    public ResponseEntity<ResponseGetWebToken> getWebToken(
            @Valid @RequestBody RequestGetWebToken requestGetWebToken,
            @RequestHeader("Ted-Id") String tedId
    ) throws AlfaMsException {

        return ResponseEntity.ok(service.getWebToken(requestGetWebToken));
    }


}
