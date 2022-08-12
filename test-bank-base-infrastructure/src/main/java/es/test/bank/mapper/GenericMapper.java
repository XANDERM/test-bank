package es.test.bank.mapper;

import java.util.List;

public interface GenericMapper<S, T> {
	
	T toDomainModel(S s);
	S toEntity(T t);
    List<S> toEntities(List<T> tList);
    List<T> toDomainModelList(List<S> sList);

}

