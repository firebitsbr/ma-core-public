/**
 * Copyright (C) 2016 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.serotonin.m2m2.module.definitions.event.detectors;

import com.serotonin.m2m2.vo.event.detector.AbstractEventDetectorVO;
import com.serotonin.m2m2.vo.event.detector.AlphanumericRegexStateDetectorVO;
import com.serotonin.m2m2.web.mvc.rest.v1.model.events.detectors.AbstractEventDetectorModel;
import com.serotonin.m2m2.web.mvc.rest.v1.model.events.detectors.AlphanumericRegexStateEventDetectorModel;

/**
 * @author Terry Packer
 *
 */
public class AlphanumericRegexStateEventDetectorDefinition extends PointEventDetectorDefinition<AlphanumericRegexStateDetectorVO>{

	public static final String TYPE_NAME = "ALPHANUMERIC_REGEX_STATE";
		
	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.module.EventDetectorDefinition#getEventDetectorSubTypeName()
	 */
	@Override
	public String getEventDetectorTypeName() {
		return TYPE_NAME;
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.module.EventDetectorDefinition#getDescriptionKey()
	 */
	@Override
	public String getDescriptionKey() {
		return "pointEdit.detectors.regexState";
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.module.EventDetectorDefinition#createEventDetectorVO()
	 */
	@Override
	protected AlphanumericRegexStateDetectorVO createEventDetectorVO() {
		return new AlphanumericRegexStateDetectorVO();
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.module.EventDetectorDefinition#createModel(com.serotonin.m2m2.vo.event.detector.AbstractEventDetectorVO)
	 */
	@Override
	public AbstractEventDetectorModel<AlphanumericRegexStateDetectorVO> createModel(
			AbstractEventDetectorVO<AlphanumericRegexStateDetectorVO> vo) {
		return new AlphanumericRegexStateEventDetectorModel((AlphanumericRegexStateDetectorVO) vo);
	}
	
	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.module.EventDetectorDefinition#getModelClass()
	 */
	@Override
	public Class<?> getModelClass() {
		return AlphanumericRegexStateEventDetectorModel.class;
	}
}
