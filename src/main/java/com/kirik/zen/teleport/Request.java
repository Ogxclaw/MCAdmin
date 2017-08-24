package com.kirik.zen.teleport;

import java.util.HashMap;
import java.util.Stack;

import org.bukkit.entity.Player;

import com.kirik.zen.core.Zen;

public class Request implements RequestRunnable {
	protected static final HashMap<Player, Stack<Request>> requests = new HashMap<Player, Stack<Request>>();
	
	public static Request getRequest(Player forPlayer) {
		return getRequest(forPlayer, null);
	}

	public static Request getRequest(Player forPlayer, Player byPlayer) {
		if(requests.containsKey(forPlayer)) {
			Stack<Request> reqs = requests.get(forPlayer);
			if(byPlayer == null) {
				return reqs.isEmpty() ? null : reqs.firstElement();
			} else {
				for(Request req : reqs) {
					if(req.byPlayer == byPlayer) {
						return req;
					}
				}
			}
		}
		return null;
	}
	
	public static void timeoutCheck(Player forPlayer) {
		@SuppressWarnings("unchecked")
		Stack<Request> reqs = (Stack<Request>)requests.get(forPlayer).clone();
		for(Request req : reqs) {
			if(!req.isInTime()) {
				req.remove();
			}
		}
	}

	public static void timeoutCheck() {
		for(Player ply : requests.keySet()) {
			timeoutCheck(ply);
		}
	}
	
	public static void removeAllRequests(Player forPlayer) {
		requests.remove(forPlayer);
	}

	private final Player forPlayer;
	private final Player byPlayer;
	private final RequestRunnable execute;
	private long timeout;
	
	private Zen plugin = Zen.instance;
	
	public Request(Player forPlayer, Player byPlayer, RequestRunnable run) {
		this.byPlayer = byPlayer;
		this.forPlayer = forPlayer;
		this.execute = run;
		this.timeout = 0;
	}

	@Override
	public void accept() {
		try {
			execute.accept();
		} catch(Exception e) { }
		remove();
	}

	@Override
	public void decline() {
		try {
			execute.decline();
		} catch(Exception e) { }
		remove();
	}

	private void remove() {
		requests.get(forPlayer).remove(this);
	}

	public void add(String msg) {
		Request req = getRequest(forPlayer, byPlayer);
		if(req != null) {
			req.remove();
		}
		this.timeout = System.currentTimeMillis() + 30000;
		if(!requests.containsKey(forPlayer)) {
			requests.put(forPlayer, new Stack<Request>());
		}
		requests.get(forPlayer).add(this);
		plugin.playerHelper.sendDirectedMessage(forPlayer, String.format(msg, forPlayer.getDisplayName(), byPlayer.getDisplayName()));
		plugin.playerHelper.sendDirectedMessage(forPlayer, "Use /tpaccept to accept or /tpdeny to decline");
	}

	private boolean isInTime() {
		return System.currentTimeMillis() <= timeout;
	}
}
