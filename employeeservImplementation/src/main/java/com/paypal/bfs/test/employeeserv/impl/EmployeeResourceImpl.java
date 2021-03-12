package com.paypal.bfs.test.employeeserv.impl;

import com.paypal.bfs.test.employeeserv.api.EmployeeResource;
import com.paypal.bfs.test.employeeserv.api.model.Address;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.entity.AddressDaoEntity;
import com.paypal.bfs.test.employeeserv.entity.EmployeeDaoEntity;
import com.paypal.bfs.test.employeeserv.repository.EmployeeResourceRepository;
import com.paypal.bfs.test.employeeserv.validation.RequestValidation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Implementation class for employee resource.
 */
@RestController
@Slf4j
public class EmployeeResourceImpl implements EmployeeResource {

    private final EmployeeResourceRepository employeeResourceRepository;

    @Autowired
    public EmployeeResourceImpl(EmployeeResourceRepository employeeResourceRepository) {
        this.employeeResourceRepository = employeeResourceRepository;
    }

    @Override
    public ResponseEntity<Employee> employeeGetById(String id) {
        try {
            EmployeeDaoEntity employeeDaoEntity = employeeResourceRepository.findById(Integer.parseInt(id)).get();
            Employee employee = new Employee();
            employee.setFirstName(employeeDaoEntity.getFirstName());
            employee.setLastName(employeeDaoEntity.getLastName());
            Address address = new Address();
            address.setLine1(employeeDaoEntity.getAddressDaoEntity().getLine1());
            address.setLine2(employeeDaoEntity.getAddressDaoEntity().getLine2());
            address.setCity(employeeDaoEntity.getAddressDaoEntity().getCity());
            address.setState(employeeDaoEntity.getAddressDaoEntity().getState());
            address.setCountry(employeeDaoEntity.getAddressDaoEntity().getCountry());
            address.setZipCode(employeeDaoEntity.getAddressDaoEntity().getZipCode());
            employee.setAddress(address);
            return new ResponseEntity<>(employee, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception occurred while calling employeeGetById() with id : {} : {}", id, ExceptionUtils.getFullStackTrace(e));
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Employee> employeeCreate(Employee employee) {

        /*
        * Validations including :
        * first name validation,
        * last name validation,
        * date of birth (dd-MM-yyyy) validation,
        * Address mandatory fields validation,
        * Idempotency logic is implemented to avoid duplicate resource creation
        * */

        if (!RequestValidation.validateFirstName(employee.getFirstName())
                || !RequestValidation.validateLasName(employee.getLastName())
                || !RequestValidation.validateJavaDate(employee.getDateOfBirth()) || !RequestValidation.validateAddress(employee.getAddress()) ||
                employeeGetById(String.valueOf(employee.getId())).getStatusCodeValue() == HttpStatus.OK.value()) {
            log.error("bad requests received while calling employeeCreate()");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        // --------------- * --------------------- * ------------------------------ //

        try {
            EmployeeDaoEntity employeeDaoEntity = EmployeeDaoEntity.builder().build();
            employeeDaoEntity.setFirstName(employee.getFirstName());
            employeeDaoEntity.setLastName(employee.getLastName());
            employeeDaoEntity.setId(employee.getId());
            employeeDaoEntity.setDateOfBirth(employee.getDateOfBirth());
            AddressDaoEntity addressDaoEntity = AddressDaoEntity
                    .builder()
                    .line1(employee.getAddress().getLine1())
                    .line2(employee.getAddress().getLine2())
                    .city(employee.getAddress().getCity())
                    .country(employee.getAddress().getCountry())
                    .state(employee.getAddress().getState())
                    .zipCode(employee.getAddress().getZipCode())
                    .id(employee.getId())
                    .build();
            employeeDaoEntity.setAddressDaoEntity(addressDaoEntity);
            employeeResourceRepository.save(employeeDaoEntity);
            return new ResponseEntity<>(employee, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception occurred while calling employeeCreate() with employee : {} : {}", employee, ExceptionUtils.getFullStackTrace(e));
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
