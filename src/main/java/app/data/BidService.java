package app.data;

import org.springframework.stereotype.Service;

@Service
public class BidService {
   /* @Autowired
  private app.data.BidRepository mRepository;
  */

  /*
  public void createBid(BidRequestBody bidRequest) {
    final app.data.BidEntity bidEntity = new app.data.BidEntity(bidRequest.getBaseCurrency(), bidRequest.getTotal(),
        bidRequest.getCurrency());
    mRepository.save(bidEntity);
  }

  public List<Bid> getAllSavedBids() {
    List<app.data.Bid> bids = new ArrayList<>();
    mRepository.findAll().forEach((entity) -> bids.add(new app.data.Bid(entity.getmBaseCurrency(),
        entity.getTotal(), entity.getCurrency())));
    return bids;
  }
*/
}
