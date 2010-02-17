package org.sonar.ide.eclipse.views.model;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.progress.IDeferredWorkbenchAdapter;
import org.eclipse.ui.progress.IElementCollector;
import org.sonar.ide.eclipse.SonarPlugin;
import org.sonar.ide.eclipse.Messages;
import org.sonar.wsclient.Host;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.services.ResourceQuery;

/**
 * @author J�r�mie Lagarde
 * 
 */
public class TreeRoot extends TreeParent implements IDeferredWorkbenchAdapter{

	public TreeRoot() {
		super(null);
	}

	@Override
  public String getName() {
		return "";
	}

	@Override
  public Sonar getServer() {
		return null;
	}

	@Override
  protected ResourceQuery createResourceQuery() {
	    return null;
	}
	
	@Override
  public void fetchDeferredChildren(Object object,
			IElementCollector collector, IProgressMonitor monitor) {
		if (!(object instanceof TreeRoot)) {
			return;
		}
		TreeRoot node = (TreeRoot) object;
		List<Host> servers = SonarPlugin.getServerManager().getServers();
		monitor.beginTask(Messages.getString("pending"), servers.size()); //$NON-NLS-1$
		for (Host server : servers) {
			TreeServer treeServer = new TreeServer(server);
			node.addChild(treeServer);
		}
		monitor.done();
	}

	@Override
	public String getRemoteURL() {
		return null;
	}


}
