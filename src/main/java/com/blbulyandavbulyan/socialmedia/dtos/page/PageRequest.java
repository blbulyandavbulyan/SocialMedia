package com.blbulyandavbulyan.socialmedia.dtos.page;

import org.springframework.data.domain.Sort;

public record PageRequest(int pageNumber, int pageSize, Sort.Direction direction) {
}
