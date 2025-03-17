package while1.kunnect.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import while1.kunnect.domain.Member;
import while1.kunnect.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        return User.builder()
                .username(member.get().getEmail())
                .password(member.get().getPassword())
                .roles(member.get().getRole().name())
                .build();
    }
}
