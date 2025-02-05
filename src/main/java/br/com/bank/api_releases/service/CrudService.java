package br.com.bank.api_releases.service;

import br.com.bank.api_releases.forms.DeleteListForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface CrudService<D, F> {


    List<D> findAll();

    Page<D> findPageable(String search, Pageable pageable) throws Exception;

    D findById(String id) throws Exception;

    @Transactional
    D create(F form) throws Exception;

    @Transactional
    D update(String id, F form) throws Exception;

    @Transactional
    void delete(String id) throws Exception;

    @Transactional
    void deleteByIdIn(DeleteListForm deleteListForm) throws Exception;
}
