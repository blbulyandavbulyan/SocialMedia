package com.blbulyandavbulyan.socialmedia.dtos;

import java.util.List;

/**
 * Предоставляет DTO для моей страницы(для оборачивания стандартного объекта {@see }
 *
 * @param content       список с контентом
 * @param totalPages    всего страниц
 * @param totalElements всего элементов(хранящихся в базе)
 * @param pageSize      размер страницы
 * @param number        номер страницы, начиная с 1
 * @param last          последняя ли страница
 * @param first         первая ли страница
 * @param <T>           тип, который будет храниться в {@link Page#content()}
 */
public record Page<T>(List<T> content, int totalPages, long totalElements,
                      int pageSize, int number, boolean last, boolean first) {
    public static <T> Page<T> of(org.springframework.data.domain.Page<T> page) {
        return new Page<>(page.getContent(), page.getTotalPages(), page.getTotalElements(), page.getSize(), page.getNumber() + 1, page.isLast(), page.isFirst());
    }
}
