package com.epam.esm.util.filters;

import com.epam.esm.models.interfaces.TagInterface;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Component
public class TagFilter {
    public List<TagInterface> filterTagsThatNeedToRemoveFromCerts(List<TagInterface> oldTags, List<TagInterface> newTags) {
        if (isNotEmpty(oldTags) || (oldTags.size() == 1 && oldTags.get(0).getName() != null)) {
            return oldTags.stream()
                    .filter(tag -> !newTags.contains(tag.getName()))
                    .toList();
        }
        return List.of();
    }
    public List<TagInterface> filterTagsThatNeedToAddToCerts(List<TagInterface> oldTags, List<TagInterface> newTags) {
        if (isNotEmpty(oldTags) || (oldTags.size() == 1 && oldTags.get(0).getName() != null)) {
            return List.copyOf(newTags.stream()
                    .filter(tag -> !oldTags.contains(tag.getName()))
                    .collect(Collectors.toSet()));
        }
        return newTags;
    }
}
