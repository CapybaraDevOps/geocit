package com.softserveinc.geocitizen.exception;

import com.softserveinc.tools.model.JsonError;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author softserveinc (https://t.me/Shrralis)
 * @version 1.0
 * Created 1/2/18 at 4:50 PM
 */
public class EntityNotExistException extends AbstractCitizenException {

	private final Entity entity;
	private final String additionalInfo;

	public EntityNotExistException(Entity entity) {
		super(entity.getError().getMessage());

		this.entity = entity;
		this.additionalInfo = null;
	}

	public EntityNotExistException(Entity entity, String additionalInfo) {
		super(entity.getError().getMessage());

		this.entity = entity;
		this.additionalInfo = additionalInfo;
	}

	public Entity getEntity() {
		return entity;
	}

	@Override
	public JsonError.Error getError() {
		return entity.getError().forField(additionalInfo);
	}

	public enum Entity {
		IMAGE,
		MAP_MARKER,
		USER,
		ISSUE,
		RECOVERY_TOKEN;

		private static final Map<Entity, JsonError.Error> ENTITY_ERROR_MAP;

		static {
			ENTITY_ERROR_MAP = new ConcurrentHashMap<>();

			ENTITY_ERROR_MAP.put(IMAGE, JsonError.Error.IMAGE_NOT_EXIST);
			ENTITY_ERROR_MAP.put(MAP_MARKER, JsonError.Error.MAP_MARKER_NOT_EXIST);
			ENTITY_ERROR_MAP.put(USER, JsonError.Error.USER_NOT_EXIST);
			ENTITY_ERROR_MAP.put(ISSUE, JsonError.Error.ISSUE_NOT_EXIST);
			ENTITY_ERROR_MAP.put(RECOVERY_TOKEN, JsonError.Error.RECOVERY_TOKEN_EXPIRED);
		}

		public static JsonError.Error getError(Entity entity) {
			return ENTITY_ERROR_MAP.get(entity);
		}

		public JsonError.Error getError() {
			return ENTITY_ERROR_MAP.get(this);
		}
	}
}
