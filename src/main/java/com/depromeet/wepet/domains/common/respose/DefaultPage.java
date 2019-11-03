package com.depromeet.wepet.domains.common.respose;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DefaultPage<T> implements Page<T>, Serializable {
    private static final DefaultPage<?> EMPTY =
            new DefaultPage(Collections.emptyList(), Pageable.unpaged(), 0) {
                @Override
                public boolean hasNext() {
                    return false;
                }

                @Override
                public String toString() {
                    return "Empty elements";
                }
            };

    @Getter
    private final Pageable pageable;
    @Getter
    private final List<T> content;
    private final long total;

    public DefaultPage(List<T> content, Pageable pageable, long total) {
        this.content = content;
        this.pageable = pageable;
        this.total = total;
    }

    private DefaultPage(List<T> content) {
        this(content, PageRequest.of(1, content.size()), content.size());
    }

    public static <T> DefaultPage<T> of(List<T> content) {
        return content == null || content.isEmpty() ? empty() : new DefaultPage<>(content);
    }

    public static <T> DefaultPage<T> of(final List<T> all, final Pageable pageable) {
        if (all == null || all.isEmpty()) {
            return empty();
        }

        int from = Math.min((pageable.getPageNumber() - 1) * pageable.getPageSize(), all.size());
        int to = Math.min(pageable.getPageNumber() * pageable.getPageSize(), all.size());

        if (from == to) {
            return empty();
        }

        return new DefaultPage<>(all.subList(from, to), pageable, all.size());
    }

    @SuppressWarnings("unchecked")
    public static <T> DefaultPage<T> empty() {
        return (DefaultPage<T>) EMPTY;
    }

    @Override
    public boolean hasNext() {
        return pageable.getPageNumber() < getTotalPages();
    }

    @Override
    public int getNumber() {
        return pageable.isPaged() ? pageable.getPageNumber() : 0;
    }

    @Override
    public long getTotalElements() {
        return total;
    }

    @Override
    public int getSize() {
        return pageable.isPaged() ? pageable.getPageSize() : 0;
    }

    @Override
    public int getTotalPages() {
        return getSize() == 0 ? 0 : (int) Math.ceil((double) total / (double) getSize());
    }

    @Override
    public Pageable nextPageable() {
        return hasNext() ? pageable.next() : Pageable.unpaged();
    }

    @Override
    public Pageable previousPageable() {
        return hasPrevious() ? pageable.previousOrFirst() : Pageable.unpaged();
    }

    @Override
    public boolean hasPrevious() {
        return getNumber() > 0;
    }

    @Override
    public boolean isFirst() {
        return !hasPrevious();
    }

    @Override
    public boolean isLast() {
        return !hasNext();
    }

    @Override
    public <U> Page<U> map(Function<? super T, ? extends U> converter) {
        return new DefaultPage<>(this.stream().map(converter).collect(Collectors.toList()), getPageable(), total);
    }

    @Override
    public int getNumberOfElements() {
        return content.size();
    }

    @Override
    public boolean hasContent() {
        return !content.isEmpty();
    }

    @Override
    public Sort getSort() {
        return pageable.getSort();
    }

    @Override
    public Iterator<T> iterator() {
        return content.iterator();
    }

    @Override
    public String toString() {
        return String.format("Page %s of %d", getNumber(), getTotalPages());
    }
}