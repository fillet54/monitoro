package com.fiftycuatro.monitoro.collectors


class CollectorGroup implements Set<Collector> {

    String id
    String name

    private Set<Collector> backingStore = new HashSet<>()

    Collector getAt(int i) {
        return backingStore.getAt(i)
    }

    @Override
    boolean add(Collector collector) {
        return backingStore.add(collector)
    }

    @Override
    boolean remove(Object o) {
        return backingStore.remove(o)
    }

    @Override
    boolean containsAll(Collection<?> c) {
        return backingStore.containsAll(c)
    }

    @Override
    boolean addAll(Collection<? extends Collector> c) {
        return backingStore.addAll(c)
    }

    @Override
    boolean retainAll(Collection<?> c) {
        return backingStore.retainAll(c)
    }

    @Override
    boolean removeAll(Collection<?> c) {
        return backingStore.removeAll(c)
    }


    @Override
    int size() {
        return backingStore.size()
    }

    @Override
    boolean isEmpty() {
        return backingStore.isEmpty()
    }

    @Override
    boolean contains(Object o) {
        return backingStore.contains()
    }

    @Override
    Iterator<Collector> iterator() {
        return backingStore.iterator()
    }

    @Override
    Object[] toArray() {
        return backingStore.toArray()
    }

    @Override
    def <T> T[] toArray(T[] a) {
        return backingStore.toArray(a)
    }

    @Override
    void clear() {
        backingStore.clear()
    }


}
