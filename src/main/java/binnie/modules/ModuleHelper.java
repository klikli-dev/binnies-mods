package binnie.modules;

import forestry.core.utils.Log;
import net.minecraftforge.fml.common.discovery.ASMDataTable;

import java.util.*;

public class ModuleHelper {
	private ModuleHelper() {

	}

	public static Map<String, List<Module>> getModules(ASMDataTable asmDataTable) {
		String annotationClassName = BinnieModule.class.getCanonicalName();
		Set<ASMDataTable.ASMData> asmDatas = asmDataTable.getAll(annotationClassName);
		List<Module> instances = new ArrayList<>();
		for (ASMDataTable.ASMData asmData : asmDatas) {
			try {
				Class<?> asmClass = Class.forName(asmData.getClassName());
				Class<? extends Module> asmInstanceClass = asmClass.asSubclass(Module.class);
				Module instance = asmInstanceClass.newInstance();
				instances.add(instance);
			} catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
				Log.error("Failed to load: {}", asmData.getClassName(), e);
			}
		}
		Map<String, List<Module>> modules = new LinkedHashMap<>();
		for(Module module : instances){
			BinnieModule info = module.getClass().getAnnotation(BinnieModule.class);
			modules.computeIfAbsent(info.moduleContainerID(), k->new ArrayList()).add(module);
		}

		return modules;
	}
}
