package de.msk.mylivetracker.web.frontend.demo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import de.msk.mylivetracker.service.demo.IDemoService;
import de.msk.mylivetracker.web.util.UrlUtils;
import de.msk.mylivetracker.web.util.request.ReqParam;

/**
 * DemoCtrl.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class DemoCtrl extends AbstractController {

	private static final Log log = LogFactory.getLog(DemoCtrl.class);
	
	private IDemoService demoService;
	
	private static final ReqParam<Boolean> PARAM_RUN = 
		new ReqParam<Boolean>("run", Boolean.class);
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
		HttpServletResponse response) throws Exception {
		
		if (PARAM_RUN.getValueFromReq(request, false)) {
			demoService.runDemo();
			log.info("Demo has been started.");
		} else {
			demoService.stopDemo();
			log.info("Demo has been stopped.");
		}
				
		return new ModelAndView("redirect:" + UrlUtils.URL_TRACKS_OVERVIEW_CTRL);
	}

	/**
	 * @return the demoService
	 */
	public IDemoService getDemoService() {
		return demoService;
	}

	/**
	 * @param demoService the demoService to set
	 */
	public void setDemoService(IDemoService demoService) {
		this.demoService = demoService;
	}		
}
