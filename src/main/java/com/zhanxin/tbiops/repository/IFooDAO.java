package com.zhanxin.tbiops.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IFooDAO extends JpaRepository<Foo, Long> {

    Foo findByUserName(String name);

}