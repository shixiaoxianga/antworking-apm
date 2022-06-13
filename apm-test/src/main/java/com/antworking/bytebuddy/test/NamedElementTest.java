package com.antworking.bytebuddy.test;

import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.field.FieldDescription;
import org.junit.Test;

import java.lang.reflect.Field;

public class NamedElementTest  {
    public String demo = "one";

    @Test
    public void print() throws Exception {
        Field demoField = NamedElementTest.class.getDeclaredField("demo");
        FieldDescription.ForLoadedField loadedField = new FieldDescription.ForLoadedField(demoField);
        System.out.println("实际名称 getActualName() : " + loadedField.getActualName());

        // 实现了NamedElement中的三个内置实现
        System.out.println("impl form WithRuntimeName : " + (loadedField instanceof NamedElement.WithRuntimeName));
        System.out.println("getName() : " + loadedField.getName());
        System.out.println("getInternalName() : " + loadedField.getInternalName());

        System.out.println("impl form WithDescriptor : " + (loadedField instanceof NamedElement.WithDescriptor));
        System.out.println("getDescriptor() : " + loadedField.getDescriptor());
        System.out.println("getGenericSignature() : " + loadedField.getGenericSignature());

        System.out.println("impl form WithGenericName : " + (loadedField instanceof NamedElement.WithGenericName));
        System.out.println("toGenericString() : " + loadedField.toGenericString());

        System.out.println("impl form WithOptionalName : " + (loadedField instanceof NamedElement.WithOptionalName));


    }

}

