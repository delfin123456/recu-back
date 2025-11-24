package utnfrc.isi.backend.repositories;

import jakarta.persistence.EntityManager;
import java.util.List;
import utnfrc.isi.backend.entities.Employee;
import utnfrc.isi.backend.repositories.context.DbContext;

public class EmployeeRepository extends Repository<Employee> {

    public EmployeeRepository() {
        super(Employee.class);
    }

    // Buscar empleados por jefe
    public List<Employee> findByManager(Integer managerId) {
        EntityManager em = DbContext.getEntityManager();
        try {
            return em.createQuery("SELECT e FROM Employee e WHERE e.reportsTo.employeeId = :managerId", Employee.class)
                    .setParameter("managerId", managerId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Buscar empleado por email
    public Employee findByEmail(String email) {
        EntityManager em = DbContext.getEntityManager();
        try {
            return em.createQuery("SELECT e FROM Employee e WHERE e.email = :email", Employee.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }
}