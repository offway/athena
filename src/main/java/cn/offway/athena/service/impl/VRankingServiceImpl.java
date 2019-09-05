package cn.offway.athena.service.impl;

import cn.offway.athena.domain.VRanking;
import cn.offway.athena.repository.VRankingRepository;
import cn.offway.athena.service.VRankingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * VIEWService接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-09-05 12:57:03 Exp $
 */
@Service
public class VRankingServiceImpl implements VRankingService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private VRankingRepository vRankingRepository;

    @Override
    public VRanking save(VRanking vRanking) {
        return vRankingRepository.save(vRanking);
    }

    @Override
    public VRanking findOne(Long id) {
        return vRankingRepository.findOne(id);
    }

    @Override
    public void delete(Long id) {
        vRankingRepository.delete(id);
    }

    @Override
    public List<VRanking> save(List<VRanking> entities) {
        return vRankingRepository.save(entities);
    }

    @Override
	public Page<VRanking> findAll(Pageable pageable){
    	return vRankingRepository.findAll(pageable);
	}
}
