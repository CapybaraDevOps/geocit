/*
 * The following code have been created by Yaroslav Zhyravov (shrralis).
 * The code can be used in non-commercial way for everyone.
 * But for any commercial way it needs a author's agreement.
 * Please contact the author for that:
 *  - https://t.me/Shrralis
 *  - https://twitter.com/Shrralis
 *  - shrralis@gmail.com
 *
 * Copyright (c) 2017 by shrralis (Yaroslav Zhyravov).
 */

package com.softserveinc.geocitizen.entity;

import com.softserveinc.geocitizen.entity.interfaces.Identifiable;
import com.softserveinc.geocitizen.util.PsqlEnum;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static com.softserveinc.geocitizen.entity.Image.TABLE_NAME;

@Entity
@Table(name = TABLE_NAME)
@TypeDef(
		name = "image_type",
		typeClass = PsqlEnum.class
)
public class Image implements Identifiable<Integer> {

	public static final String TABLE_NAME = "images";
	public static final String TYPE_COLUMN_NAME = "type";
	public static final String ID_COLUMN_NAME = "id";
	public static final String SRC_COLUMN_NAME = "src";
	public static final String HASH_COLUMN_NAME = "hash";
	public static final int MAX_SRC_LENGTH = 128;
	public static final int MIN_SRC_LENGTH = 4;
	public static final int MAX_HASH_LENGTH = 32;
	public static final int MIN_HASH_LENGTH = MAX_HASH_LENGTH;

	@Id
//	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "images_seq_gen")
	@SequenceGenerator(name = "images_seq_gen", sequenceName = "images_id_seq", allocationSize = 1)
	@Column(name = ID_COLUMN_NAME/*, nullable = false*/, unique = true)
	private Integer id;

	//	@NotNull
	@Enumerated(EnumType.STRING)
	@org.hibernate.annotations.Type(type = "image_type")
	@Column(name = TYPE_COLUMN_NAME/*, nullable = false*/)
	private Type type = Type.ISSUE;

	@NotBlank
	@Size(min = MIN_SRC_LENGTH, max = MAX_SRC_LENGTH)
	@Column(name = SRC_COLUMN_NAME/*, nullable = false*/, length = MAX_SRC_LENGTH)
	private String src;

	@NotBlank
	@Size(min = MIN_HASH_LENGTH, max = MAX_HASH_LENGTH)
	@Column(name = HASH_COLUMN_NAME/*, nullable = false*/, length = MAX_HASH_LENGTH)
	private String hash;

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Image.Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public enum Type {
		USER,
		ISSUE
	}
}