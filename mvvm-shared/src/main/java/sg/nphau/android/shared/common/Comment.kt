/*
 * Created by nphau on 21/03/2022, 00:30
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 21/03/2022, 20:23
 */
package sg.nphau.android.shared.common

@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.TYPEALIAS,
    AnnotationTarget.EXPRESSION
)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class Comment(
    val message: String
)
