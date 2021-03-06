package org.black_ixx.bossshop.core;

import java.io.File;
import java.util.HashMap;
import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.config.BSConfigShop;
import org.black_ixx.bossshop.managers.config.DefaultCreator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BSShops {

	public BSShops(BossShop plugin){
		shops = new HashMap<Integer, BSShop>();
		shopsIds = new HashMap<String, Integer>();
		
		
		File folder = new File(plugin.getDataFolder().getAbsolutePath()+ "/shops/");
		if (!folder.isFile()&!folder.isDirectory()){

			createDefaults();
			
		}
		for (File f : folder.listFiles()){
			if (f!=null){
				if (!f.isDirectory()){
					if (f.isFile()){
						
						if (f.getName().contains(".yml")){
						loadShop(f);
						}
						
						
					}
				}
			}			
		}
		
		Bukkit.getLogger().info("[BossShop] Loaded "+shops.size()+" Shops!");
		
		
	}
	
	/////////////////////////////// <- Variables
	
	private HashMap<Integer, BSShop> shops;
	private HashMap<String, Integer> shopsIds;
	
	/////////////////////////////// <- Load Shop
	
	public void addShop(BSShop shop){
		shops.put(shop.getShopId(), shop);
		
		if(shopsIds.containsKey(shop.getShopName().toLowerCase())){
		ClassManager.manager.getBugFinder().warn("Two Shops with the same Name ("+shop.getShopName().toLowerCase()+") are loaded. When opening a Shop via Name, only one of this Shops will be opened!");
		}
		
		shopsIds.put(shop.getShopName().toLowerCase(), shop.getShopId());
	}
	
	public BSShop loadShop(File f){
		String name = f.getName();
		BSShop shop = new BSConfigShop(createId(), name);
		
		addShop(shop);
		
		return shop;
	}
	
	public void unloadShop(BSShop shop){
		int id = getShopId(shop.getShopName());
		shopsIds.remove(shop.getShopName());
		shops.remove(id);
		shop.close();
	}
		
	
	/////////////////////////////// <- Simple Methods

	public void openShop(Player p, String name){
		if (!isShop(name)){
			ClassManager.manager.getMessageHandler().sendMessage("Main.ShopNotExisting", p);
			return;
		}
		
		openShop(p, getShopFast(name));
		
	}

	public void openShop(Player p, BSShop shop){		
		shop.openInventory(p);
		ClassManager.manager.getMessageHandler().sendMessage("Main.OpenShop", p);
	}

	public BSShop getShop(String name){
		return getShop(getShopId(name));
	}
	
	public BSShop getShopFast(String name){
		return getShopFast(getShopId(name));
	}

	public BSShop getShop(int id){
		return shops.containsKey(id)?shops.get(id):null;
	}
	
	public BSShop getShopFast(int id){
		return shops.get(id);
	}
	
	public int getShopId(String name){
		if (!shopsIds.containsKey(name)){
			//ClassManager.manager.getBugFinder().warn("Was not able to get the Id of the "+name+" Shop.");
			return -1; //Was return 0 before. Changed because I think then it returns no shop for sure!
		}
		return shopsIds.get(name);
	}
	
	public boolean isShop(String name){
		return shopsIds.containsKey(name);
	}

	public boolean isShop(int id){
		return shops.containsKey(id);
	}
	
	public HashMap<Integer, BSShop> getShops(){
		return shops;
	}
	

	public HashMap<String, Integer> getShopIds(){
		return shopsIds;
	}
	
	public int createId(){
		id++;
		return id;
	}
	
	private int id = 0;
	
	////////////////////////////////////////////////////////////////////////////
	
	public void createDefaults(){
		
		//ExampleShop
		
		DefaultCreator creator = new DefaultCreator();
		
		creator.addAllExamples();
		
	}
	

}
