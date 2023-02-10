package com.project.organizacion.service;

import com.project.organizacion.dto.EmpleadoDto;
import com.project.organizacion.entity.Empleado;
import com.project.organizacion.entity.Empresa;
import com.project.organizacion.entity.Rol;
import com.project.organizacion.repository.EmpleadoRepository;
import com.project.organizacion.repository.EmpresaRepository;
import com.project.organizacion.repository.RolRepository;
import com.project.organizacion.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmpleadoServiceImpl implements IEmpleadoService {

    @Autowired
    EmpleadoRepository empleadoRepository;

    @Autowired
    EmpresaRepository empresaRepository;

    @Autowired
    RolRepository rolRepository;


    @Override
    public List<EmpleadoDto> listarEmpleados() {
        return empleadoRepository.findAll().stream().map(empleado -> {
            return Utils.toEmpleadoDto(empleado, Utils.toEmpresaDto(empleado.getEmpresa()), Optional.ofNullable(empleado.getRoles()).map(roles -> { return roles.stream().map(Utils::toRolDto).collect(Collectors.toList());}).orElse(null));
        }).collect(Collectors.toList());

    }

    @Override
    public List<EmpleadoDto> filtrarEmpleadosPorNombre(String texto) {
        return empleadoRepository.findByNombreContaining(texto).stream().map(empleado -> Utils.toEmpleadoDto(empleado, Utils.toEmpresaDto(empleado.getEmpresa()), Optional.ofNullable(empleado.getRoles()).map(roles -> roles.stream().map(Utils::toRolDto).collect(Collectors.toList())).orElse(null))).collect(Collectors.toList());
    }

    @Override
    public EmpleadoDto registrarEmpleado(EmpleadoDto body) {
        Optional<Empresa> empresa = empresaRepository.findById(body.getEmpresaId());
        if (empresa.isPresent()) {
            Empleado empleadoRegister = new Empleado(null, body.getNombre(), body.getSexo(), body.getTelefono(), empresa.get(), null);
            Empleado empleadoRegistered = empleadoRepository.save(empleadoRegister);
            return Utils.toEmpleadoDto(empleadoRegistered, Utils.toEmpresaDto(empleadoRegistered.getEmpresa()), Optional.ofNullable(empleadoRegistered.getRoles()).map(roles -> roles.stream().map(Utils::toRolDto).collect(Collectors.toList())).orElse(null));
        }
        return null;
    }

    @Override
    public EmpleadoDto actualizar(EmpleadoDto body, Long idEmpleado) {
        Optional<Empleado> encontrado = empleadoRepository.findById(idEmpleado);
        Optional<Empresa> empresa = empresaRepository.findById(body.getEmpresaId());
        List<Rol> rolList = rolRepository.findAllById(body.getRolesId());
        if (encontrado.isPresent() && empresa.isPresent() && rolList.size() == body.getRolesId().size()) {
            Empleado empleadoUpdate = encontrado.get();
            empleadoUpdate.setId(idEmpleado);
            empleadoUpdate.setNombre(body.getNombre());
            empleadoUpdate.setSexo(body.getSexo());
            empleadoUpdate.setTelefono(body.getTelefono());
            empleadoUpdate.setEmpresa(empresa.get());
            empleadoUpdate.setRoles(rolList);
            Empleado empleadoUpdated = empleadoRepository.save(empleadoUpdate);
            return Utils.toEmpleadoDto(empleadoUpdated, Utils.toEmpresaDto(empleadoUpdated.getEmpresa()),
                    Optional.ofNullable(empleadoUpdated.getRoles())
                            .map(roles -> roles.stream().map(Utils::toRolDto).collect(Collectors.toList())).orElse(null));
        }
        return null;
    }

    @Override
    public String eliminar(Long idEmpleado) {
        Optional<Empleado> encontrado = empleadoRepository.findById(idEmpleado);
        if (encontrado.isPresent()) {
            empleadoRepository.deleteById(idEmpleado);
            return "Empleado eliminado correctamente";
        }
        return "Empleado no se encuentra registrado";

    }
}
