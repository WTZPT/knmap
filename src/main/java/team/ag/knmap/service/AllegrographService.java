package team.ag.knmap.service;

import team.ag.knmap.commom.ServerResponse;
import team.ag.knmap.vo.TriadVo;

public interface AllegrographService {

    public void AddTriad(TriadVo triadVo);

    public ServerResponse searchGraphInfoByKey(String repositoryID, String key);
}
