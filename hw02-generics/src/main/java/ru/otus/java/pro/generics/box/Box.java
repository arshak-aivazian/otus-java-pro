package ru.otus.java.pro.generics.box;

import ru.otus.java.pro.generics.fruit.Fruit;

import java.util.ArrayList;
import java.util.List;

public class Box<T extends Fruit> {
    private final List<T> fruits;

    public Box(List<T> fruits) {
        this.fruits = new ArrayList<>(fruits);
    }

    public void addFruit(T fruit) {
        fruits.add(fruit);
    }

    public int weight() {
        return fruits.stream().mapToInt(Fruit::getWeight).sum();
    }

    public boolean compare(Box<?> box) {
        return box != null && this.weight() == box.weight();
    }

    public void moveFruitsTo(Box<? super T> consumer) {
        if (consumer == null) {
            return;
        }
        fruits.forEach(consumer::addFruit);
        fruits.clear();
    }
}
