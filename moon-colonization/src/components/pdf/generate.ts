import { jsPDF } from 'jspdf';
import autoTable from 'jspdf-autotable';
import type { UserOptions } from 'jspdf-autotable';

import { feature_mono } from './base64.js';
import { parsing } from './parse.js';
import { pngBase64 } from './logotype';
import { Success } from "../success/success";
import { User } from "../user/userInfo";
import { Statistic } from "../statistic/statistic";

interface TableInfo {
    userName: string;
    countDay: number;
    tables: {
        name: string;
        headers: string[];
        body: (string | number)[][];
    }[];
}

interface TableData {
    name: string;
    headers: string[];
    body: (string | number)[][];
}

let padding: number = 4;
let posY: number = 2;
let widthPage: number = 0;
let heightPage: number = 0;
const fontName: string = 'feature_mono_Regular';
let countPage: number = 1;
let tables_info: TableInfo;

function generatePDF(success: Success, userInfo: User, stat: Statistic): void {
    try {
        const doc = new jsPDF();
        
        widthPage = doc.internal.pageSize.getWidth();
        heightPage = doc.internal.pageSize.getHeight();
        
        // Добавляем шрифт
        doc.addFileToVFS(`${fontName}.ttf`, feature_mono);
        doc.addFont(`${fontName}.ttf`, fontName, 'normal');
        doc.setFont(fontName);
        tables_info = parsing(success, userInfo, stat);
        
        // Шапка
        posY = createHeader(doc);

        // Успешность + Ресурсы + Связь
        firstPage(doc);
        createFootor(doc);
        doc.addPage();
        createHeader(doc);
        secondPage(doc);
        createFootor(doc);

        for(let i = 3; i < tables_info.tables.length; i += 2) {
            doc.addPage();
            createHeader(doc);
            nextPage(doc, i);
            createFootor(doc);
        }
        
        doc.save('result.pdf');
    } catch (error) {
        console.error('Ошибка:', error);
        if (error instanceof Error) {
            alert('Ошибка: ' + error.message);
        } else {
            alert('Произошла неизвестная ошибка');
        }
    }
}

function secondPage(doc: jsPDF): void {
    let Ypos: number = posY;
    doc.setTextColor(0);
    doc.text(tables_info.tables[2].name, padding, posY);
    posY += 3;
    
    const tableOptions: UserOptions = {
        //head: [tables_info.tables[2].headers],
        body: tables_info.tables[2].body,
        startY: posY,
        margin: { left: padding, right: padding },
        tableWidth: 'wrap',
        styles: {
            font: fontName
        },
        headStyles: {
            font: fontName,
            textColor: '#A3A3A3',
            fillColor: 255
        },
        didDrawPage: function(data) {
            if (data.cursor) {
                Ypos = data.cursor.y;
              } else {
                Ypos = posY; // или другое значение по умолчанию
              }
        }
    };

    autoTable(doc, tableOptions);
    posY = Ypos;
    posY += 8;

    doc.setDrawColor('#A3A3A3');  
    doc.setLineWidth(0.5);
    doc.line(padding, posY, widthPage - padding, posY);
    posY += 8;
}

function createHeader(doc: jsPDF): number {
    posY = 2;
    doc.addImage(pngBase64, 'PNG', padding, posY, 73, 11);
    posY += 11;

    doc.setFontSize(12);
    doc.setTextColor('#A3A3A3');

    let un: string = tables_info.userName;
    if (un.length > 20) {
        un = un.substring(0, 20) + "...";
    }
    
    doc.text("Сделал пользователь " + un, widthPage - padding, posY - 5, {
        align: "right"
    });
    doc.text("за внутриигровых дней: " + tables_info.countDay, widthPage - padding, posY, {
        align: "right"
    });
    posY += 3;

    doc.setDrawColor('#A3A3A3');  
    doc.setLineWidth(0.5);
    doc.line(padding, posY, widthPage - padding, posY);
    posY += 12;
    
    return posY;
}

function firstPage(doc: jsPDF): void {
    let Ypos: number = posY;
    for(let i = 0; i < 2; i++) {
        doc.setTextColor(0);
        doc.text(tables_info.tables[i].name, padding, posY);
        posY += 3;
        
        const tableOptions: UserOptions = {
            body: tables_info.tables[i].body,
            startY: posY,
            margin: { left: padding, right: padding },
            tableWidth: 'wrap',
            styles: {
                font: fontName
            },
            didDrawPage: function(data) {
                if (data.cursor) {
                    Ypos = data.cursor.y;
                  } else {
                    Ypos = posY; // или другое значение по умолчанию
                  }
            }
        };

        autoTable(doc, tableOptions);
        posY = Ypos;
        posY += 8;

        doc.setDrawColor('#A3A3A3');  
        doc.setLineWidth(0.5);
        doc.line(padding, posY, widthPage - padding, posY);
        posY += 8;
    }
}

function nextPage(doc: jsPDF, j: number): void {
    for(let i = 0; i < 2; i++) {
        doc.setTextColor(0);
        doc.text(tables_info.tables[i + j].name, padding, posY);
        posY += 3;
        
        let Ypos: number = posY;
        let startPage: number = doc.getNumberOfPages();
        
        const tableOptions: UserOptions = {
            //head: [tables_info.tables[i + j].headers],
            body: tables_info.tables[i + j].body,
            startY: Ypos,
            margin: { left: padding, right: padding, top: 25, bottom: 16 },
            tableWidth: 'wrap',
            styles: {
                font: fontName
            },
            headStyles: {
                font: fontName,
                textColor: '#A3A3A3',
                fillColor: 255
            },
            didDrawPage: function(data) {
                if(doc.getNumberOfPages() > startPage) {
                    createHeader(doc);
                }
                if (data.cursor) {
                    Ypos = data.cursor.y;
                  } else {
                    Ypos = posY; // или другое значение по умолчанию
                  }
                
                const remaining: number = heightPage - Ypos;
                if (remaining < 16 + 10) {
                    createFootor(doc);
                    doc.addPage();
        
                    // Обновляем начальную позицию для нового содержимого
                    posY = 20; // Начальный отступ на новой странице
                    
                    // Перерисовываем шапку на новой странице
                    createHeader(doc);
                }
            }
        };

        autoTable(doc, tableOptions);
        posY = Ypos;
        posY += 8;

        doc.setDrawColor('#A3A3A3');  
        doc.setLineWidth(0.5);
        doc.line(padding, posY, widthPage - padding, posY);
        posY += 8;
    }
}

function createFootor(doc: jsPDF): void {
    posY = heightPage - 16;

    doc.setDrawColor('#A3A3A3');  
    doc.setLineWidth(0.5);
    doc.line(padding, posY, widthPage - padding, posY);
    posY += 12;

    let text: string = '' + countPage;
    if(countPage < 10) text = '0' + text;
    
    doc.setFontSize(12);
    doc.setTextColor('#A3A3A3');
    doc.text("Страница", padding, posY);
    doc.text(text, widthPage - padding, posY, {align: "right"});

    countPage += 1;
}

export { generatePDF };