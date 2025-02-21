package com.itti.digital.atm.atm_authorization_api.controller;

import com.itti.digital.atm.atm_authorization_api.AtmAuthorizationApiApplication;
import com.itti.digital.atm.atm_authorization_api.errors.AlfaMsException;
import com.itti.digital.atm.atm_authorization_api.errors.ErrorBody;
import com.itti.digital.atm.atm_authorization_api.models.authorization.RequestAuthorization;
import com.itti.digital.atm.atm_authorization_api.models.authorization.ResponseAuthorization;
import com.itti.digital.atm.atm_authorization_api.service.AuthorizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;



/**
 * @author Giancarlo Migliore
 */

@CrossOrigin(origins = {"*"})
@RestController
@Tag(name = "authorization")
@RequestMapping("/authorization")
public class AuthorizationController {
    private final AuthorizationService service;

    public AuthorizationController(AuthorizationService service) {
        this.service = service;
    }


    @Operation(
            description = "Servicio de obtencion de servicios y cuentas Wepa expuestos para belltech",
            operationId = "processAtmTransaction",
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
    @PostMapping("/processAtmTransaction")
    @SecurityRequirement(name = "Bearer Authentication")
    @Secured({"ROLE_USER","ROLE_ADMIN"})
    public ResponseEntity<ResponseAuthorization> processAtmTransaction(
            @Valid @RequestBody RequestAuthorization request
    )  throws AlfaMsException {
        return ResponseEntity.ok(service.processAtmTransaction(request));
    }


}
