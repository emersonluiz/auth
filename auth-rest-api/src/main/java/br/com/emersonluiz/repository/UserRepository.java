package br.com.emersonluiz.repository;

import br.com.emersonluiz.model.User;

public interface UserRepository {

    User findOne(String userName, String password);

    User findOne(String id) throws Exception;
}
