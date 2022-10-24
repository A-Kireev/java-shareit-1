package ru.practicum.shareit.item.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImpl implements ItemService {

  private final ItemDao storage;

  @Override
  public ItemDto createItem(long userId, ItemDto itemDto) {
    var item = storage.createItem(ItemMapper.toItem(itemDto, userId, null));
    return ItemMapper.toItemDto(item);
  }

  @Override
  public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
    var itemPreviousVersion = storage.getItem(itemId);
    var updatedItem = Item.builder()
        .id(itemId)
        .name(itemDto.getName() != null ? itemDto.getName() : itemPreviousVersion.getName())
        .description(itemDto.getDescription() != null ? itemDto.getDescription() : itemPreviousVersion.getDescription())
        .isAvailable(itemDto.getIsAvailable() != null ? itemDto.getIsAvailable() : itemPreviousVersion.getIsAvailable())
        .build();

    var item = storage.updateItem(updatedItem);
    return ItemMapper.toItemDto(item);
  }

  @Override
  public ItemDto getItem(long userId, Long itemId) {
    return ItemMapper.toItemDto(storage.getItem(itemId));
  }

  @Override
  public List<ItemDto> getItems(Long ownerId) {
    return storage.getItems(ownerId).stream()
        .map(ItemMapper::toItemDto)
        .collect(Collectors.toList());
  }

  @Override
  public List<ItemDto> searchItem(String searchCriteria) {
    return storage.searchItem(searchCriteria).stream()
        .map(ItemMapper::toItemDto)
        .collect(Collectors.toList());
  }
}
