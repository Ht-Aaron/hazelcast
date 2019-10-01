/*
 * Copyright (c) 2008-2019, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.sql.model;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.replicatedmap.ReplicatedMap;
import com.hazelcast.sql.model.person.City;
import com.hazelcast.sql.model.person.Department;
import com.hazelcast.sql.model.person.Person;
import com.hazelcast.sql.model.person.PersonKey;

/**
 * Utility class which generate models for test cases.
 */
public class ModelGenerator {
    public static final int CITY_CNT = 2;
    public static final int DEPARTMENT_CNT = 2;
    public static final int PERSON_CNT = 10;

    private ModelGenerator() {
        // No-op.
    }

    public static void generatePerson(HazelcastInstance member) {
        ReplicatedMap<Long, City> cityMap = member.getReplicatedMap("city");
        IMap<Long, Department> departmentMap = member.getMap("department");
        IMap<PersonKey, Person> personMap = member.getMap("person");

        for (int i = 0; i < CITY_CNT; i++) {
            cityMap.put((long) i, new City("city-" + i));
        }

        for (int i = 0; i < DEPARTMENT_CNT; i++) {
            departmentMap.put((long) i, new Department("department-" + i));
        }

        int age = 40;
        long salary = 1000;

        for (int i = 0; i < PERSON_CNT; i++) {
            PersonKey key = new PersonKey(i, i % DEPARTMENT_CNT);

            Person val = new Person(
                "person-" + i,
                age++ % 80,
                salary * (i + 1),
                i % CITY_CNT
            );

            personMap.put(key, val);
        }
    }
}