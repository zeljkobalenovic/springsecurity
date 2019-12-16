package zeljko.springsecurity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import zeljko.springsecurity.model.User;

/**
 * UserRepository
 */

public interface UserRepository extends JpaRepository<User,Long> {

    Optional <User> findByUsername(String username);

    // spring je u stanju da sam napravi implementaciju svega sto postuje naming konvencije kao sto sam ja gore 
    // ako je bas nesto zaguljeno morao bih sam da pisem implementaciju metode koju sam naveo u interfejsu
}