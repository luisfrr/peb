package gob.yucatan.persistence.specification;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class EntitySpecification<T> implements Specification<T> {

    private final List<CustomSpecification> customSpecificationList; // Lista de criterios de búsqueda dinámicos
    private final List<CustomFetch> customFetchList; // Lista de relaciones a cargar con fetch

    @Getter
    @Setter
    private Boolean selectDistinct; // Define si la consulta debe ser distinta (sin duplicados)

    public EntitySpecification() {
        this.customSpecificationList = new ArrayList<>();
        this.customFetchList = new ArrayList<>();
        this.selectDistinct = false;
    }

    // Agrega un criterio de búsqueda a la lista
    public void add(CustomSpecification criteria) {
        customSpecificationList.add(criteria);
    }

    // Agrega una relación a la lista de fetch
    public void add(CustomFetch fetch) {
        customFetchList.add(fetch);
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>(customSpecificationList.size()); // Inicializa con capacidad estimada

        // Procesa los criterios de búsqueda
        for (CustomSpecification criteria : customSpecificationList) {
            Path<?> path = null;

            // Manejo de campos anidados en entidades relacionadas
            if(criteria.isSubField()) {
                List<String> fieldPath = criteria.getFieldPath();
                for (String field : fieldPath) {
                    path = (path == null) ? root.get(field) : path.get(field);
                }
            }

            // Construcción de predicados según la operación de búsqueda
            switch (criteria.getFilterOperation()) {
                case GREATER_THAN:
                    predicates.add(criteriaBuilder.greaterThan(
                            criteria.isSubField() ? Objects.requireNonNull(path).get(criteria.getField()) : root.get(criteria.getField()),
                            criteria.getValue().toString()));
                    break;
                case LESS_THAN:
                    predicates.add(criteriaBuilder.lessThan(
                            criteria.isSubField() ? Objects.requireNonNull(path).get(criteria.getField()) : root.get(criteria.getField()),
                            criteria.getValue().toString()));
                    break;
                case GREATER_THAN_EQUAL:
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                            criteria.isSubField() ? Objects.requireNonNull(path).get(criteria.getField()) : root.get(criteria.getField()),
                            criteria.getValue().toString()));
                    break;
                case LESS_THAN_EQUAL:
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(
                            criteria.isSubField() ? Objects.requireNonNull(path).get(criteria.getField()) : root.get(criteria.getField()),
                            criteria.getValue().toString()));
                    break;
                case NOT_EQUAL:
                    predicates.add(criteriaBuilder.notEqual(
                            criteria.isSubField() ? Objects.requireNonNull(path).get(criteria.getField()) : root.get(criteria.getField()),
                            criteria.getValue()));
                    break;
                case EQUAL:
                    predicates.add(criteriaBuilder.equal(
                            criteria.isSubField() ? Objects.requireNonNull(path).get(criteria.getField()) : root.get(criteria.getField()),
                            criteria.getValue()));
                    break;
                case MATCH: // LIKE con comodines para búsqueda parcial
                    predicates.add(criteriaBuilder.like(
                            criteriaBuilder.lower(criteria.isSubField() ? Objects.requireNonNull(path).get(criteria.getField()) : root.get(criteria.getField())),
                            "%" + criteria.getValue().toString().toLowerCase() + "%"));
                    break;
                case MATCH_END:
                    predicates.add(criteriaBuilder.like(
                            criteriaBuilder.lower(criteria.isSubField() ? Objects.requireNonNull(path).get(criteria.getField()) : root.get(criteria.getField())),
                            criteria.getValue().toString().toLowerCase() + "%"));
                    break;
                case MATCH_START:
                    predicates.add(criteriaBuilder.like(
                            criteriaBuilder.lower(criteria.isSubField() ? Objects.requireNonNull(path).get(criteria.getField()) : root.get(criteria.getField())),
                            "%" + criteria.getValue().toString().toLowerCase()));
                    break;
                case IN:
                    predicates.add(criteriaBuilder.in(
                                    criteria.isSubField() ? Objects.requireNonNull(path).get(criteria.getField()) : root.get(criteria.getField()))
                            .value(criteria.getValue()));
                    break;
                case NOT_IN:
                    predicates.add(criteriaBuilder.in(
                                    criteria.isSubField() ? Objects.requireNonNull(path).get(criteria.getField()) : root.get(criteria.getField()))
                            .value(criteria.getValue()).not());
                    break;
                case IS_NOT_NULL:
                    predicates.add(criteriaBuilder.isNotNull(
                            criteria.isSubField() ? Objects.requireNonNull(path).get(criteria.getField()) : root.get(criteria.getField())));
                    break;
                case IS_NULL:
                    predicates.add(criteriaBuilder.isNull(
                            criteria.isSubField() ? Objects.requireNonNull(path).get(criteria.getField()) : root.get(criteria.getField())));
                    break;
            }
        }

        // Manejo de fetch para cargar relaciones
        for (CustomFetch customFetch : customFetchList) {
            FetchParent<?, ?> fetchParent = root;
            for (String field : customFetch.getFieldPath()) {
                fetchParent = fetchParent.fetch(field, customFetch.getJoinType());
            }
            fetchParent.fetch(customFetch.getField(), customFetch.getJoinType());
        }

        // Aplica la opción de selección distinta si no está ya activada
        if(Boolean.FALSE.equals(query.isDistinct()) && Boolean.TRUE.equals(this.getSelectDistinct())) {
            query.distinct(true);
        }

        // Retorna la conjunción de todos los predicados generados
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
