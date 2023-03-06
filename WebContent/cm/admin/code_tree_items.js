var TREE_ITEMS = [
 ['전체품목','CodeMgrServlet?mode=list_item',
    ['F/G CODE','CodeMgrServlet?mode=list_item&&category=F'],
    ['ASSY 코드','CodeMgrServlet?mode=list_item&&category=1'],
    ['회로부품코드(표준)','CodeMgrServlet?mode=list_item&&category=2',
       ['CAPACITOR','CodeMgrServlet?mode=list_item&&category=200',
          ['CAP AL ELEC','CodeMgrServlet?mode=list_item&&category=20001'],
          ['CAP CER','CodeMgrServlet?mode=list_item&&category=20002'],
          ['CAP PLA FILM','CodeMgrServlet?mode=list_item&&category=20003'],
          ['CAP TAN','CodeMgrServlet?mode=list_item&&category=20004'],
          ['CAP VAR','CodeMgrServlet?mode=list_item&&category=20005'],
          ['CAP NET','CodeMgrServlet?mode=list_item&&category=20006'],
          ['CAP OTH','CodeMgrServlet?mode=list_item&&category=20007'],
       ],
       ['FUSE','CodeMgrServlet?mode=list_item&&category=201',
          ['FUSE','CodeMgrServlet?mode=list_item&&category=20101'],
          ['POLY SWITCH','CodeMgrServlet?mode=list_item&&category=20102'],
       ],
       ['INDUCTOR','CodeMgrServlet?mode=list_item&&category=202',
          ['IND','CodeMgrServlet?mode=list_item&&category=20201'],
          ['IND POWER','CodeMgrServlet?mode=list_item&&category=20202'],
          ['CHOKE COIL','CodeMgrServlet?mode=list_item&&category=20203'],
          ['BEAD','CodeMgrServlet?mode=list_item&&category=20204'],
          ['POWER FILTER','CodeMgrServlet?mode=list_item&&category=20205'],
          ['ANALOG FILTER','CodeMgrServlet?mode=list_item&&category=20206'],
          ['IND VAR','CodeMgrServlet?mode=list_item&&category=20207'],
       ],
       ['RESISTOR','CodeMgrServlet?mode=list_item&&category=203',
          ['RES CHIP','CodeMgrServlet?mode=list_item&&category=20301'],
          ['RES CF','CodeMgrServlet?mode=list_item&&category=20302'],
          ['RES MF','CodeMgrServlet?mode=list_item&&category=20303'],
          ['RES MOF','CodeMgrServlet?mode=list_item&&category=20304'],
          ['RES MGF','CodeMgrServlet?mode=list_item&&category=20305'],
          ['RES NET','CodeMgrServlet?mode=list_item&&category=20306'],
          ['RES VR','CodeMgrServlet?mode=list_item&&category=20307'],
          ['RES OT','CodeMgrServlet?mode=list_item&&category=20308'],
       ],
       ['THERMISTOR','CodeMgrServlet?mode=list_item&&category=204',
          ['THERMISTOR','CodeMgrServlet?mode=list_item&&category=20401'],
          ['VARISTOR','CodeMgrServlet?mode=list_item&&category=20402'],
       ],
       ['TRANSFORMER','CodeMgrServlet?mode=list_item&&category=205',
          ['TRANS AT','CodeMgrServlet?mode=list_item&&category=20501'],
          ['TRANS PT','CodeMgrServlet?mode=list_item&&category=20502'],
          ['TRANS PTT','CodeMgrServlet?mode=list_item&&category=20503'],
          ['TRANS RFT','CodeMgrServlet?mode=list_item&&category=20504'],
          ['TRANS FBT','CodeMgrServlet?mode=list_item&&category=20505'],
          ['TRANS FCT','CodeMgrServlet?mode=list_item&&category=20506'],
          ['TRANS IFT','CodeMgrServlet?mode=list_item&&category=20507'],
          ['TRANS OTHER','CodeMgrServlet?mode=list_item&&category=20508'],
       ],
       ['DIODE','CodeMgrServlet?mode=list_item&&category=215',
          ['DIODE BRID','CodeMgrServlet?mode=list_item&&category=21501'],
          ['DIODE REGUL','CodeMgrServlet?mode=list_item&&category=21502'],
          ['DIODE ARR','CodeMgrServlet?mode=list_item&&category=21503'],
          ['DIODE PIN','CodeMgrServlet?mode=list_item&&category=21504'],
          ['DIODE RECT','CodeMgrServlet?mode=list_item&&category=21505'],
          ['DIODE REC','CodeMgrServlet?mode=list_item&&category=21506'],
          ['DIODE SCHOTTKY','CodeMgrServlet?mode=list_item&&category=21507'],
          ['DIODE SW','CodeMgrServlet?mode=list_item&&category=21508'],
          ['DIODE TUNNEL','CodeMgrServlet?mode=list_item&&category=21509'],
          ['DIODE TVS','CodeMgrServlet?mode=list_item&&category=21510'],
          ['DIODE VAR','CodeMgrServlet?mode=list_item&&category=21511'],
          ['DIODE ZENER','CodeMgrServlet?mode=list_item&&category=21512'],
          ['DIODE OTHER','CodeMgrServlet?mode=list_item&&category=21513'],
       ],
       ['OSCILLATOR','CodeMgrServlet?mode=list_item&&category=216',
          ['OSCILLATOR CO','CodeMgrServlet?mode=list_item&&category=21601'],
          ['OSCILLATOR RO','CodeMgrServlet?mode=list_item&&category=21602'],
          ['OSCILLATOR TCXO','CodeMgrServlet?mode=list_item&&category=21603'],
          ['OSCILLATOR VCO','CodeMgrServlet?mode=list_item&&category=21604'],
          ['OSCILLATOR VCXO','CodeMgrServlet?mode=list_item&&category=21605'],
       ],
       ['THYRISTOR','CodeMgrServlet?mode=list_item&&category=217',
          ['THYRISTOR DIAC','CodeMgrServlet?mode=list_item&&category=21701'],
          ['THYRISTOR SCR','CodeMgrServlet?mode=list_item&&category=21702'],
          ['THYRISTOR DIDAC','CodeMgrServlet?mode=list_item&&category=21703'],
          ['THYRISTOR TRIAC','CodeMgrServlet?mode=list_item&&category=21704'],
       ],
       ['TRANSISTOR','CodeMgrServlet?mode=list_item&&category=218',
          ['TR NPN','CodeMgrServlet?mode=list_item&&category=21801'],
          ['TR PNP','CodeMgrServlet?mode=list_item&&category=21802'],
          ['TR NJFET','CodeMgrServlet?mode=list_item&&category=21803'],
          ['TR PJFET','CodeMgrServlet?mode=list_item&&category=21804'],
          ['TR NMOSFET','CodeMgrServlet?mode=list_item&&category=21805'],
          ['TR PMOSFET','CodeMgrServlet?mode=list_item&&category=21806'],
          ['TR IGBT','CodeMgrServlet?mode=list_item&&category=21807'],
          ['TR MOSFET ARRAY','CodeMgrServlet?mode=list_item&&category=21808'],
          ['TR ARR','CodeMgrServlet?mode=list_item&&category=21809'],
       ],
       ['PROCESSOR','CodeMgrServlet?mode=list_item&&category=230',
          ['PROCESSOR','CodeMgrServlet?mode=list_item&&category=23001'],
          ['PROCESSOR CONTROLLER','CodeMgrServlet?mode=list_item&&category=23002'],
          ['PROCESSOR DSP','CodeMgrServlet?mode=list_item&&category=23003'],
       ],
       ['MEMORY','CodeMgrServlet?mode=list_item&&category=231',
          ['MEMORY FIFO','CodeMgrServlet?mode=list_item&&category=23101'],
          ['MEMORY RAM','CodeMgrServlet?mode=list_item&&category=23102'],
          ['MEMORY ROM/FLASH','CodeMgrServlet?mode=list_item&&category=23103'],
          ['MEMORY MODULE','CodeMgrServlet?mode=list_item&&category=23104'],
       ],
       ['PLD','CodeMgrServlet?mode=list_item&&category=232',
          ['PLD','CodeMgrServlet?mode=list_item&&category=23201'],
          ['FPGA','CodeMgrServlet?mode=list_item&&category=23202'],
       ],
       ['LOGIC IC(74xx)','CodeMgrServlet?mode=list_item&&category=233',
          ['IC B/D','CodeMgrServlet?mode=list_item&&category=23301'],
          ['IC COM','CodeMgrServlet?mode=list_item&&category=23302'],
          ['IC COUNTER','CodeMgrServlet?mode=list_item&&category=23303'],
          ['IC D/D','CodeMgrServlet?mode=list_item&&category=23304'],
          ['IC ENCODER','CodeMgrServlet?mode=list_item&&category=23305'],
          ['IC F/F','CodeMgrServlet?mode=list_item&&category=23306'],
          ['IC G/I','CodeMgrServlet?mode=list_item&&category=23307'],
          ['IC LATCH','CodeMgrServlet?mode=list_item&&category=23308'],
          ['IC TRA','CodeMgrServlet?mode=list_item&&category=23309'],
          ['IC TIMER','CodeMgrServlet?mode=list_item&&category=23310'],
          ['IC SW','CodeMgrServlet?mode=list_item&&category=23311'],
          ['IC S/M/D','CodeMgrServlet?mode=list_item&&category=23312'],
          ['IC SCHMITT T/G','CodeMgrServlet?mode=list_item&&category=23313'],
          ['IC REGISTER','CodeMgrServlet?mode=list_item&&category=23314'],
          ['IC PARITY C/G','CodeMgrServlet?mode=list_item&&category=23315'],
          ['IC MULT','CodeMgrServlet?mode=list_item&&category=23316'],
       ],
       ['CLOCK MANAGEMENT','CodeMgrServlet?mode=list_item&&category=234',
          ['CLOCK D/B','CodeMgrServlet?mode=list_item&&category=23401'],
          ['CLOCK GEN','CodeMgrServlet?mode=list_item&&category=23402'],
       ],
       ['DATA CONVERTER','CodeMgrServlet?mode=list_item&&category=235',
          ['DC A2DC','CodeMgrServlet?mode=list_item&&category=23501'],
          ['DC D2AC','CodeMgrServlet?mode=list_item&&category=23502'],
       ],
       ['OTHER IC','CodeMgrServlet?mode=list_item&&category=236',
          ['OIC UART','CodeMgrServlet?mode=list_item&&category=23601'],
          ['OIC LINE DRIVER','CodeMgrServlet?mode=list_item&&category=23602'],
          ['OIC OTHER ICS','CodeMgrServlet?mode=list_item&&category=23603'],
          ['OIC U/P SUPERVISORY','CodeMgrServlet?mode=list_item&&category=23604'],
          ['OCI RTC','CodeMgrServlet?mode=list_item&&category=23605'],
          ['OIC DELAY LINE','CodeMgrServlet?mode=list_item&&category=23606'],
          ['OIC E/P/U','CodeMgrServlet?mode=list_item&&category=23607'],
          ['OIC SUBSCRIBER','CodeMgrServlet?mode=list_item&&category=23608'],
          ['OIC V/A','CodeMgrServlet?mode=list_item&&category=23609'],
       ],
       ['AMPLIFIER&RF','CodeMgrServlet?mode=list_item&&category=237',
          ['A&RF AMPLIFIER','CodeMgrServlet?mode=list_item&&category=23701'],
          ['A&RF ATTENUATOR','CodeMgrServlet?mode=list_item&&category=23702'],
          ['A&RF CIRCULATOR','CodeMgrServlet?mode=list_item&&category=23703'],
          ['A&RF COUPLER','CodeMgrServlet?mode=list_item&&category=23704'],
          ['A&RF DIVIDER','CodeMgrServlet?mode=list_item&&category=23705'],
          ['A&RF DOUBLER','CodeMgrServlet?mode=list_item&&category=23706'],
          ['A&RF LIMITER','CodeMgrServlet?mode=list_item&&category=23707'],
          ['A&RF MIXER','CodeMgrServlet?mode=list_item&&category=23708'],
          ['A&RF DETECTOR','CodeMgrServlet?mode=list_item&&category=23709'],
          ['A&RF SHIFTER','CodeMgrServlet?mode=list_item&&category=23710'],
          ['A&RF SYN','CodeMgrServlet?mode=list_item&&category=23711'],
          ['A&RF M/D','CodeMgrServlet?mode=list_item&&category=23712'],
          ['A&RF S/C','CodeMgrServlet?mode=list_item&&category=23713'],
          ['A&RF ISOLATOR','CodeMgrServlet?mode=list_item&&category=23714'],
          ['A&RF CON','CodeMgrServlet?mode=list_item&&category=23715'],
          ['A&RF SW','CodeMgrServlet?mode=list_item&&category=23716'],
       ],
       ['POWER MANAGEMENT','CodeMgrServlet?mode=list_item&&category=238',
          ['PM VC','CodeMgrServlet?mode=list_item&&category=23801'],
          ['PM VCON','CodeMgrServlet?mode=list_item&&category=23802'],
          ['PM VREF','CodeMgrServlet?mode=list_item&&category=23803'],
          ['PM VREG','CodeMgrServlet?mode=list_item&&category=23804'],
          ['PM PWR D/C','CodeMgrServlet?mode=list_item&&category=23805'],
       ],
       ['DISPLAY','CodeMgrServlet?mode=list_item&&category=245',
          ['DIS LCD','CodeMgrServlet?mode=list_item&&category=24501'],
          ['DIS LED DISPLAY','CodeMgrServlet?mode=list_item&&category=24502'],
          ['DIS VFD','CodeMgrServlet?mode=list_item&&category=24503'],
       ],
       ['FIBER OPTIC','CodeMgrServlet?mode=list_item&&category=246'],
       ['OPTO ELECTRICAL','CodeMgrServlet?mode=list_item&&category=247',
          ['OE LED','CodeMgrServlet?mode=list_item&&category=24701'],
          ['OE LASER DIODE','CodeMgrServlet?mode=list_item&&category=24702'],
          ['OE PC','CodeMgrServlet?mode=list_item&&category=24703'],
          ['OE PD','CodeMgrServlet?mode=list_item&&category=24704'],
          ['OE PI','CodeMgrServlet?mode=list_item&&category=24705'],
          ['OE PTR','CodeMgrServlet?mode=list_item&&category=24706'],
          ['OE LAMP','CodeMgrServlet?mode=list_item&&category=24707'],
       ],
       ['CONNECTOR','CodeMgrServlet?mode=list_item&&category=260',
          ['CONNECTOR BC','CodeMgrServlet?mode=list_item&&category=26001'],
          ['CONNECTOR B2C','CodeMgrServlet?mode=list_item&&category=26002'],
          ['CONNECTOR B2B','CodeMgrServlet?mode=list_item&&category=26003'],
          ['CONNECTOR B2W','CodeMgrServlet?mode=list_item&&category=26004'],
          ['CONNECTOR FF','CodeMgrServlet?mode=list_item&&category=26005'],
          ['CONNECTOR I/O','CodeMgrServlet?mode=list_item&&category=26006'],
          ['CONNECTOR R/A/V','CodeMgrServlet?mode=list_item&&category=26007'],
       ],
       ['RELAY','CodeMgrServlet?mode=list_item&&category=261',
          ['RELAY','CodeMgrServlet?mode=list_item&&category=26101'],
       ],
       ['SWITCH','CodeMgrServlet?mode=list_item&&category=262',
          ['SW DIP','CodeMgrServlet?mode=list_item&&category=26201'],
          ['SW KP','CodeMgrServlet?mode=list_item&&category=26202'],
          ['SW PB','CodeMgrServlet?mode=list_item&&category=26203'],
          ['SW MICRO','CodeMgrServlet?mode=list_item&&category=26204'],
          ['SW ROTARY','CodeMgrServlet?mode=list_item&&category=26205'],
          ['SW SLIDE','CodeMgrServlet?mode=list_item&&category=26206'],
          ['SW TACT','CodeMgrServlet?mode=list_item&&category=26207'],
          ['SW TOGGLE','CodeMgrServlet?mode=list_item&&category=26208'],
          ['SW TS','CodeMgrServlet?mode=list_item&&category=26209'],
          ['SW JS','CodeMgrServlet?mode=list_item&&category=26210'],
       ],
       ['BUZZER','CodeMgrServlet?mode=list_item&&category=275',
          ['BUZZER PB','CodeMgrServlet?mode=list_item&&category=27501'],
          ['BUZZER MB','CodeMgrServlet?mode=list_item&&category=27502'],
       ],
       ['BATTERY','CodeMgrServlet?mode=list_item&&category=276',
          ['BATTERY SCB','CodeMgrServlet?mode=list_item&&category=27601'],
          ['BATTERY RB','CodeMgrServlet?mode=list_item&&category=27602'],
          ['BATTERY BH','CodeMgrServlet?mode=list_item&&category=27603'],
    ],    ],
    ['회로부품코드(사양)','CodeMgrServlet?mode=list_item&&category=3'],
    ['기구부품코드(표준)','CodeMgrServlet?mode=list_item&&category=4',
       ['SCREW','CodeMgrServlet?mode=list_item&&category=401',
          ['SCREW MC','CodeMgrServlet?mode=list_item&&category=40101'],
          ['SCREW TAP','CodeMgrServlet?mode=list_item&&category=40102'],
          ['SCREW SELP TAP','CodeMgrServlet?mode=list_item&&category=40103'],
       ],
       ['BOLTS','CodeMgrServlet?mode=list_item&&category=402',
          ['BOLT HEX','CodeMgrServlet?mode=list_item&&category=40201'],
          ['BOLT SQUARE','CodeMgrServlet?mode=list_item&&category=40202'],
          ['BOLT EYE','CodeMgrServlet?mode=list_item&&category=40203'],
          ['BOLT STEP','CodeMgrServlet?mode=list_item&&category=40204'],
          ['BOLT ANCHOR','CodeMgrServlet?mode=list_item&&category=40205'],
       ],
       ['NUTS','CodeMgrServlet?mode=list_item&&category=403',
          ['NUT HEX','CodeMgrServlet?mode=list_item&&category=40301'],
          ['NUT SQUARE','CodeMgrServlet?mode=list_item&&category=40302'],
       ],
       ['RIVETS','CodeMgrServlet?mode=list_item&&category=404',
          ['RIVET','CodeMgrServlet?mode=list_item&&category=40401'],
       ],
       ['WASHER','CodeMgrServlet?mode=list_item&&category=405',
          ['WASHER PLANE','CodeMgrServlet?mode=list_item&&category=40501'],
          ['WASHER SPRING','CodeMgrServlet?mode=list_item&&category=40502'],
          ['WASHER TOOTH','CodeMgrServlet?mode=list_item&&category=40503'],
          ['WASHER STOP','CodeMgrServlet?mode=list_item&&category=40504'],
       ],
       ['SPRING','CodeMgrServlet?mode=list_item&&category=406',
          ['SPRING','CodeMgrServlet?mode=list_item&&category=40601'],
    ],    ],
    ['기구부품코드(사양)','CodeMgrServlet?mode=list_item&&category=5',
       ['금속류','CodeMgrServlet?mode=list_item&&category=501',
          ['FRAME','CodeMgrServlet?mode=list_item&&category=50101'],
          ['GUIDE','CodeMgrServlet?mode=list_item&&category=50102'],
          ['HOUSING','CodeMgrServlet?mode=list_item&&category=50103'],
          ['WIRE','CodeMgrServlet?mode=list_item&&category=50104'],
          ['PANEL','CodeMgrServlet?mode=list_item&&category=50105'],
          ['COVER','CodeMgrServlet?mode=list_item&&category=50106'],
          ['HOLDER','CodeMgrServlet?mode=list_item&&category=50107'],
          ['DOOR','CodeMgrServlet?mode=list_item&&category=50108'],
          ['BUCKET','CodeMgrServlet?mode=list_item&&category=50109'],
       ],
       ['수지,RUBBER류','CodeMgrServlet?mode=list_item&&category=502',
          ['GASKET','CodeMgrServlet?mode=list_item&&category=50201'],
          ['DAMPER','CodeMgrServlet?mode=list_item&&category=50202'],
          ['FILTER','CodeMgrServlet?mode=list_item&&category=50203'],
          ['COVER','CodeMgrServlet?mode=list_item&&category=50204'],
          ['HOLDER','CodeMgrServlet?mode=list_item&&category=50205'],
       ],
       ['종이,LABEL','CodeMgrServlet?mode=list_item&&category=503',
          ['BOX','CodeMgrServlet?mode=list_item&&category=50301'],
          ['LOGO PLATE','CodeMgrServlet?mode=list_item&&category=50302'],
          ['NAME PLATE','CodeMgrServlet?mode=list_item&&category=50303'],
       ],
       ['목재류','CodeMgrServlet?mode=list_item&&category=504',
          ['목재류','CodeMgrServlet?mode=list_item&&category=50401'],
       ],
       ['복합재질류','CodeMgrServlet?mode=list_item&&category=505',
          ['복합재질류','CodeMgrServlet?mode=list_item&&category=50501'],
       ],
       ['기타류','CodeMgrServlet?mode=list_item&&category=506',
          ['기타류','CodeMgrServlet?mode=list_item&&category=50601'],
       ],
       ['화공약품류(원자재)','CodeMgrServlet?mode=list_item&&category=507',
          ['화공약품류(원자재)','CodeMgrServlet?mode=list_item&&category=50701'],
       ],
    ],
 ],
];
