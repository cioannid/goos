package goos;

import static goos.FakeAuctionServer.XMPP_HOSTNAME;
import static goos.Main.AUCTION_RESOURCE;
import static goos.SniperState.JOINING;
import static goos.SniperState.BIDDING;
import static goos.SniperState.WINNING;
import static goos.SniperState.LOST;
import static goos.SniperState.WON;


public class ApplicationRunner {
	public static final String SNIPER_ID = "sniper";
	public static final String SNIPER_PASSWORD = "sniper";
	public static final String SNIPER_XMPP_ID = SNIPER_ID + "@" + XMPP_HOSTNAME + "/" + AUCTION_RESOURCE;
	private AuctionSniperDriver driver;
	private String itemId;
	
	public void startBiddingIn(final FakeAuctionServer auction) {
		itemId = auction.getItemId();
		
		Thread thread = new Thread("Test Application") {
			@Override public void run() {
				try {
					Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		thread.setDaemon(true);
		thread.start();
		driver = new AuctionSniperDriver(1000);
		driver.showsSniperStatus("", 0, 0, SnipersTableModel.textFor(JOINING));
	}

	public void hasShownSniperIsBidding(int lastPrice, int lastBid) {
		driver.showsSniperStatus(itemId, lastPrice, lastBid, SnipersTableModel.textFor(BIDDING));
	}

	public void hasShownSniperIsWinning(int winningBid) {
		driver.showsSniperStatus(itemId, winningBid, winningBid, SnipersTableModel.textFor(WINNING));
	}

	public void showsSniperHasLostAuction(int lastPrice, int lastBid) {
		driver.showsSniperStatus(itemId, lastPrice, lastBid, SnipersTableModel.textFor(LOST));
	}

	public void showsSniperHasWonAuction(int lastPrice) {
		driver.showsSniperStatus(itemId, lastPrice, lastPrice, SnipersTableModel.textFor(WON));		
	}
	
	public void stop() {
		if (driver != null) {
			driver.dispose();
		}
	}
}
