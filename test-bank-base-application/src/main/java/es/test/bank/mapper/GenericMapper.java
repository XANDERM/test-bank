package es.test.bank.mapper;

import java.util.List;

public interface GenericMapper<S, T> {
	
	T toDTO(S s);
	S toModel(T t);
	List<T> toDTOList(List<S> sList);
	List<S> toModelList(List<T> tList);

}

