package service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itiaoling.service.template.business.service.interfaces.TestService;
import com.itiaoling.service.template.infra.mybatis.entity.BamAccount;
import com.itiaoling.service.template.infra.mybatis.mapper.BamAccountMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author charles
 * @since 2023/12/15
 */
@Slf4j
@Service
public class TestServiceImpl implements TestService {

    @Resource
    BamAccountMapper bamAccountMapper;

    @Override
    public void test() {
        LambdaQueryWrapper<BamAccount> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BamAccount::getProjectCode, "SPF");
        List<BamAccount> bamAccounts = bamAccountMapper.selectList(queryWrapper);
        log.info("test :{}", JSON.toJSONString(bamAccounts));
    }
}
