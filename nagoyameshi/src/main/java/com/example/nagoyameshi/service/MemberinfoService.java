package com.example.nagoyameshi.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Memberinfo;
import com.example.nagoyameshi.entity.Role;
import com.example.nagoyameshi.form.SignupForm;
import com.example.nagoyameshi.form.UserEditForm;
import com.example.nagoyameshi.repository.MemberinfoRepository;
import com.example.nagoyameshi.repository.RoleRepository;

@Service
public class MemberinfoService {

    private final MemberinfoRepository memberinfoRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberinfoService(MemberinfoRepository memberinfoRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.memberinfoRepository = memberinfoRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ここに isMailaddress メソッドを追加します
    public boolean isMailaddress(UserEditForm userEditForm) {
        Memberinfo memberinfo = memberinfoRepository.getReferenceById(userEditForm.getId());
        return !memberinfo.getMailaddress().equals(userEditForm.getMailaddress());
    }

    @Transactional
    public Memberinfo create(SignupForm signupForm) {
        Memberinfo memberinfo = new Memberinfo();

        // "ROLE_GENERAL" がデータベースに事前に存在していることを前提としています。
        Role role = roleRepository.findByName("ROLE_GENERAL");
        if (role == null) {
            System.out.println("role_idにはnullが入っています");
        } else {
            System.out.println("role_idにはnullは入っていません");
        }

        memberinfo.setName(signupForm.getName());
        memberinfo.setFurigana(signupForm.getFurigana());
        memberinfo.setPostal_code(signupForm.getPostal_code());
        memberinfo.setAddress(signupForm.getAddress());
        memberinfo.setPhone_number(signupForm.getPhone_number());
        memberinfo.setMailaddress(signupForm.getMailaddress());
        memberinfo.setPassword(passwordEncoder.encode(signupForm.getPassword()));
        memberinfo.setRole(role);
        memberinfo.setEnabled(false);

        return memberinfoRepository.save(memberinfo);
    }

    @Transactional
    public void update(UserEditForm userEditForm) {
        Memberinfo memberinfo = memberinfoRepository.getReferenceById(userEditForm.getId());

        memberinfo.setName(userEditForm.getName());
        memberinfo.setFurigana(userEditForm.getFurigana());
        memberinfo.setPostal_code(userEditForm.getPostal_code());
        memberinfo.setAddress(userEditForm.getAddress());
        memberinfo.setPhone_number(userEditForm.getPhone_number());
        memberinfo.setMailaddress(userEditForm.getMailaddress());

        memberinfoRepository.save(memberinfo);
    }

    public boolean isMailaddressRegistered(String mailaddress) {
        Memberinfo memberinfo = memberinfoRepository.findByMailaddress(mailaddress);
        return memberinfo != null;
    }

    public boolean isSamePassword(String password, String passwordConfirmation) {
        return password.equals(passwordConfirmation);
    }

    // ユーザーを有効にする
    @Transactional
    public void enableUser(Memberinfo user) {
        user.setEnabled(true);
        memberinfoRepository.save(user);
    }

    public static void createVerificationTokenForMemberinfo(String mailaddress, String token) {
        
    }

    public String getUserRoleByEmail(String email) {
        Memberinfo memberinfo = memberinfoRepository.findByMailaddress(email);
        if (memberinfo != null) {
            return memberinfo.getRole().getName();
        } else {
            throw new RuntimeException("User not found with email: " + email);
        }
    }

    @Transactional
    public void upgradeUserRole(String mailaddress, String newRoleName) {
        Memberinfo memberinfo = memberinfoRepository.findByMailaddress(mailaddress);
        if (memberinfo != null) {
            Role newRole = roleRepository.findByName(newRoleName);
            if (newRole != null) {
                memberinfo.setRole(newRole);
                memberinfoRepository.save(memberinfo);
            } else {
                throw new RuntimeException("Role not found: " + newRoleName);
            }
        } else {
            throw new RuntimeException("Member not found with email: " + mailaddress);
        }
    }

    // ユーザーのロールを変更するメソッド
    @Transactional
    public boolean changeUserRole(String mailaddress, String newRoleName) {
        Memberinfo memberinfo = memberinfoRepository.findByMailaddress(mailaddress);
        if (memberinfo != null) {
            Role newRole = roleRepository.findByName(newRoleName);
            if (newRole != null) {
                memberinfo.setRole(newRole);
                memberinfoRepository.save(memberinfo);
                return true;
            } else {
                throw new RuntimeException("Role not found: " + newRoleName);
            }
        } else {
            throw new RuntimeException("Member not found with email: " + mailaddress);
        }
    }

    public String getCustomerIdByEmail(String email) {
        Memberinfo memberinfo = memberinfoRepository.findByMailaddress(email);
        if (memberinfo != null) {
            return memberinfo.getCustomerId(); // ここでCustomer IDを返す
        } else {
            throw new RuntimeException("User not found with email: " + email);
        }
    }

    // Customer IDを設定するメソッドを追加
    @Transactional
    public void setCustomerIdForUser(String email, String customerId) {
        Memberinfo memberinfo = memberinfoRepository.findByMailaddress(email);
        if (memberinfo != null) {
            memberinfo.setCustomerId(customerId);
            memberinfoRepository.save(memberinfo);
        } else {
            throw new RuntimeException("Member not found with email: " + email);
        }
    }
    public String getRoleDisplayName(String roleName) {
        switch (roleName) {
            case "ROLE_GENERAL": return "無料会員";
            case "ROLE_PAID": return "有料会員";
            case "ROLE_ADMIN": return "管理者";
            default: return "不明";
        }
        
    }
    
}