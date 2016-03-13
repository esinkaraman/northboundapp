package tr.edu.boun.cmpe.swe599.nothbound.info;

public class Actions {

	private String set_eth_dst;
	private String set_eth_src;
	private String set_ipv4_dst;
	private String set_ipv4_src;
	private String output;

	public String getSet_eth_dst() {
		return set_eth_dst;
	}

	public void setSet_eth_dst(String set_eth_dst) {
		this.set_eth_dst = set_eth_dst;
	}

	public String getSet_eth_src() {
		return set_eth_src;
	}

	public void setSet_eth_src(String set_eth_src) {
		this.set_eth_src = set_eth_src;
	}

	public String getSet_ipv4_dst() {
		return set_ipv4_dst;
	}

	public void setSet_ipv4_dst(String set_ipv4_dst) {
		this.set_ipv4_dst = set_ipv4_dst;
	}

	public String getSet_ipv4_src() {
		return set_ipv4_src;
	}

	public void setSet_ipv4_src(String set_ipv4_src) {
		this.set_ipv4_src = set_ipv4_src;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (set_eth_dst != null) {
			builder.append("set_eth_dst=");
			builder.append(set_eth_dst);
			builder.append(",");
		}
		if (set_eth_src != null) {
			builder.append("set_eth_src=");
			builder.append(set_eth_src);
			builder.append(",");
		}
		if (set_ipv4_dst != null) {
			builder.append("set_ipv4_dst=");
			builder.append(set_ipv4_dst);
			builder.append(",");
		}
		if (set_ipv4_src != null) {
			builder.append("set_ipv4_src=");
			builder.append(set_ipv4_src);
			builder.append(",");
		}
		if (output != null) {
			builder.append("output=");
			builder.append(output);
		}
		return builder.toString();
	}

	public static void main(String[] args) {
		Actions a = new Actions();
		a.set_eth_dst = "kkk";
		System.out.println(a.toString());
	}

}