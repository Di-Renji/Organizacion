package com.project.organizacion.controller;

import com.project.organizacion.dto.EmpresaDto;
import com.project.organizacion.service.IEmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empresa")
public class EmpresaController {

    @Autowired
    private IEmpresaService iEmpresaService;

    @GetMapping("/saludo")
    public String mostrarSaludo(){
        return "Hola mundo xD";
    }

    @GetMapping(value = "/listar")
    public List<EmpresaDto> listar(){
        return iEmpresaService.listar();
    }

    @GetMapping(value = "/buscar/{id}")
    public EmpresaDto buscar(@PathVariable Long id){
        return iEmpresaService.buscar(id);
    }

    @PostMapping(value = "/registrar")
    public EmpresaDto registar(@RequestBody EmpresaDto empresaDto){
        return iEmpresaService.registrar(empresaDto);
    }

    @PutMapping(value = "/actualizar/{id}")
    public EmpresaDto actualizar(@PathVariable Long id, @RequestBody EmpresaDto empresaDto){
        return iEmpresaService.actualizar(id, empresaDto);
    }

    @DeleteMapping(value = "/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        return iEmpresaService.eliminar(id);
    }


}
