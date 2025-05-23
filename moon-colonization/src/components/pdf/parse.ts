const nameModules: string[] = [
    "Жилой модуль", "Жилой модуль", "Административный модуль", "Спортивный модуль",
    "Медецинский модуль", "Плантация", "Исследовательский модуль", "Исследовательский модуль",
    "Исследовательский модуль", "Исследовательский модуль", "Коридор", "Административный модуль",
    "Солнечная электростанция", "Ремонтный модуль", "Космодром", "Вышка связи",
    "Мусорный полигон", "Мусорный полигон", "Производственное предприятие", "Производственное предприятие",
    "Астрономическая площадка", "База шахты", "Склад", "Склад", "Склад", "Склад"
];

const coordinates: string[] = [
    "87° 48' 44\" ю.ш. 57° 33' 31\" в.д.",
    "87° 4' 4\" ю.ш. 67° 40' 13\" в.д.",
    "89° 22' 46\" ю.ш. 54° 44' 29\" в.д.",
    "89° 41' 7\" ю.ш. 48° 39' 6\" в.д.",
    "89° 21' 32\" ю.ш. 124° 34' 2\" в.д.",
    "88° 44' 32\" ю.ш. 144° 45' 39\" в.д."
];

const nameZones: string[] = ["Равнина 1", "Равнина 2", "Высота 1", "Высота 2", "Низина 1", "Низина 2"];

import { Success } from "../success/success";
import { User } from "../user/userInfo";
import { Statistic } from "../statistic/statistic";
import { ZoneLink } from "../colony/zones";
import { Module } from "../modules/modules";
import { ZoneProduction } from "../statistic/ZoneProduction";

interface Table {
    name: string;
    headers: string[];
    body: (string | number)[][];
}

interface TablesInfo {
    userName: string;
    countDay: number;
    tables: Table[];
}

function parsing(success: Success, userInfo: User, stat: Statistic): TablesInfo {
    const tables_info: TablesInfo = {
        userName: userInfo.name,
        countDay: stat.countDay,
        tables: []
    };

    tables_info.tables.push(
        {
            name: "Успешность - " + success.successful + ":",
            headers: [],
            body: [
                ["1.", "Настроение", success.mood],
                ["2.", "Количество ресурсов", Math.min(100, (success.contPeople - success.needContPeople) / 8)],
                ["3.", "Состояние ресурсов", success.resources],
                ["4.", "Централизация", success.central],
                ["5.", "Тэмп исследования", success.search],
            ]
        }
    );

    tables_info.tables.push(
        {
            name: "Ресурсы:",
            headers: [],
            body: [
                ["1.", "Вода", stat.countResources[0]],
                ["2.", "Топливо", stat.countResources[1]],
                ["3.", "Провизия", stat.countResources[2]],
                ["4.", "Электричество", stat.countResources[3]],
                ["5.", "Кислород", stat.countResources[4]],
                ["6.", "Углекислый газ", stat.countResources[5]],
                ["7.", "Мусор", stat.countResources[6]],
                ["8.", "Материалы", stat.countResources[7]],
            ]
        }
    );

    tables_info.tables.push(
        {
            name: "Связь между областями:",
            headers: [],
            body: getLink(userInfo.links)
        }
    );

    for (let i = 0; i < nameZones.length; i++) {
        const zones: ZoneProduction[] = [...stat.zoneProductions];
        zones.sort((a, b) => a.id - b.id);

        tables_info.tables.push(
            {
                name: `${nameZones[i]} (${coordinates[i]}):`,
                headers: [],
                body: [
                    ["№", "Ресурс", "Производство", "Потребление"],
                    ["1.", "Вода", zones[i].production[0], zones[i].consumption[0]],
                    ["2.", "Топливо", zones[i].production[1], zones[i].consumption[1]],
                    ["3.", "Провизия", zones[i].production[2], zones[i].consumption[2]],
                    ["4.", "Электричество", zones[i].production[3], zones[i].consumption[3]],
                    ["5.", "Кислород", zones[i].production[4], zones[i].consumption[4]],
                    ["6.", "Углекислый газ", zones[i].production[5], zones[i].consumption[5]],
                    ["7.", "Мусор", zones[i].production[6], zones[i].consumption[6]],
                    ["8.", "Материалы", zones[i].production[7], zones[i].consumption[7]],
                ]
            }
        );

        tables_info.tables.push(
            {
                name: "Объекты:",
                headers: [],
                body: getModulesInZone(userInfo.modules, i)
            }
        );
    }

    return tables_info;
}

function getLink(links: ZoneLink[]): (string | number)[][] {
    const res: (string | number)[][] = [];
    res.push(["Область 1", "Область 2", "Маршрут", "Электрический провод"])
    for (let i = 0; i < nameZones.length; i++) {
        for (let j = i + 1; j < nameZones.length; j++) {
            const t: (string | number)[] = [];
            t.push(nameZones[i]);
            t.push(nameZones[j]);
            const tl = findLinkByZones(links, i, j);
            t.push(tl[1] == 1 ? "есть" : "нет");
            t.push(tl[0] == 1 ? "есть" : "нет");
            res.push(t);
        }
    }

    return res;
}

function findLinkByZones(links: ZoneLink[], id1: number, id2: number): number[] {
    const res: number[] = [0, 0];

    links.forEach(element => {
        if (element.id_zone1 == id1 && element.id_zone2 == id2) {
            res[element.type] = 1;
        }
    });

    return res;
}

function getModulesInZone(modules: Module[], idZone: number): (string | number)[][] {
    const res: (string | number)[][] = [];
    const sortedModules = [...modules].sort((a, b) => a.id - b.id);
    res.push(["№", "Модуль", "X", "Y"])
    console.log(res)
    let c = 1;
    sortedModules.forEach((e) => {
        if (e.idZone == idZone) {
            res.push([`${c}.`, nameModules[e.moduleType], e.x, e.y]);
            c++;
        }
    });

    return res;
}

export { parsing };
