package main

import (
	"encoding/json"
	"flag"
	"fmt"
	"github.com/kylelemons/go-gypsy/yaml"
	"github.com/shirou/gopsutil/cpu"
	"github.com/shirou/gopsutil/disk"
	"github.com/shirou/gopsutil/mem"
	"github.com/shirou/gopsutil/net"
	"io/ioutil"
	"net/http"
	"regexp"
	"strings"
	"time"
)

//#全局变量(默认配置)
//数据提交地址
var serverUri string = "http://localhost:14046/umc/gather/basic"
//频率,多少毫秒执行一次
var delay time.Duration = 10000
//网卡
var netCard string = "eth0"
//配置文件路径
var confPath string = "conf.yml"

//gather port
var port string = "22,6380"

//#返回
//id
var id string = "UNKNOW";



//初始化
func init()  {
	//get conf path
	flag.StringVar(&confPath, "p", "conf.yml", "conf path")
	flag.Parse()
	//flag.Usage()//usage
	fmt.Println("confPath="+confPath)
	//读取配置--read config
	config, err := yaml.ReadFile(confPath)
	if err != nil {
		fmt.Println(err)
	}else{
		serverUri,err = (config.Get("server-uri"))
		delayb,_ := (config.GetInt("physical.delay"))
		delay = time.Duration(delayb)
		netCard,err = (config.Get("physical.net"))
		port,_ = config.Get("physical.gatherPort")
	}
	fmt.Printf("config:serverUri=%v  delay=%v  net=%v\n",serverUri,time.Duration.String(delay),netCard)

	//init
	getId()


}

//主函数
func main() {
	//死循环
	/*for true {
		get()
		time.Sleep(delay * time.Millisecond)
	}*/
	//get()

	go memThread()
	go cpuThread()
	go diskThread()
	go netThread()

	/*for true {
		time.Sleep(100000 * time.Millisecond)
	}*/

	//netThread()

}

//mem
func memThread()  {
	for true {
		var result Result
		v, _ := mem.VirtualMemory()
		fmt.Printf("Total: %v, Free:%v, UsedPercent:%f%%\n", v.Total, v.Free, v.UsedPercent)
		//fmt.Println(v)

		result.Id = id
		result.Type = "mem"
		result.Mem = v

		fmt.Println("result = "+String(result))
		post(result)
		time.Sleep(delay * time.Millisecond)
	}
}

//cpu
func cpuThread()  {
	for true {
		var result Result
		//p, _ := cpu.Percent(0, true)
		p, _ := cpu.Times(true)

		fmt.Println(p)
		/*pa, _ := cpu.Percent(10000* time.Millisecond, true)
		fmt.Println(pa)*/
		result.Id = id
		result.Type = "cpu"
		//result.Cpu = p
		post(result)
		time.Sleep(delay * time.Millisecond)
	}
}

//disk
func diskThread()  {
	for true {
		var result Result
		disks := getDisk()
		fmt.Println(disks)

		result.Id = id
		result.Type = "disk"
		result.Disks = disks
		post(result)
		time.Sleep(delay * time.Millisecond)
	}

}

//net
func netThread()  {
	ports := strings.Split(port, ",")
	for true {
		var result Result
		//n, _ := net.IOCounters(true)
		//fmt.Println(n)
		//te, _ := net.Interfaces()
		//fmt.Println(te)
		var n []NetInfo
		for _, p := range ports {
			re := getNet(p)
			res := strings.Split(re, " ")
			if(len(res)==9){
				var netinfo NetInfo
				netinfo.Port = p
				netinfo.Up = res[0]
				netinfo.Down = res[1]
				netinfo.Count = res[2]
				netinfo.Estab = res[3]
				netinfo.CloseWait = res[4]
				netinfo.TimeWait = res[5]
				netinfo.Close = res[6]
				netinfo.Listen = res[7]
				netinfo.Closing = res[8]
				n = append(n, netinfo)
			}
		}
		result.Id = id
		result.Type = "net"
		result.Net = n
		post(result)
		time.Sleep(delay * time.Millisecond)
	}
}

//提交数据
func post(r Result) {
	data := String(r)
	request, _ := http.NewRequest("POST", serverUri,strings.NewReader(data))
	//json
	request.Header.Set("Content-Type", "application/json")
	//post数据并接收http响应
	resp,err :=http.DefaultClient.Do(request)
	if err!=nil{
		fmt.Printf("post data error:%v\n",err)
	}else {
		fmt.Println("post a data successful.")
		respBody,_ :=ioutil.ReadAll(resp.Body)
		fmt.Printf("response data:%v\n",string(respBody))
	}
}

func getDisk()  []Disk {
	partitionStats, _ := disk.Partitions(false)
	var disks [] Disk
	for _, value := range partitionStats {
		var disk1 Disk
		mountpoint := value.Mountpoint
		usageStat,_ := disk.Usage(mountpoint)
		disk1.PartitionStat = value
		disk1.Usage = *usageStat
		disks = append(disks, disk1)
	}
	return disks
}

type Disk struct {
	PartitionStat disk.PartitionStat `json:"partitionStat"`
	Usage disk.UsageStat `json:"usage"`
}

type NetInfo struct {
	Port string `json:"port"`
	Up string `json:"up"`
	Down string `json:"down"`
	Count string `json:"count"`
	Estab string `json:"estab"`
	CloseWait string `json:"closeWait"`
	TimeWait string `json:"timeWait"`
	Close string `json:"close"`
	Listen string `json:"listen"`
	Closing string `json:"closing"`
}

type Result struct {
	Id string `json:"id"`
	Type string `json:"type"`
	Mem *mem.VirtualMemoryStat `json:"mem"`
	Cpu []float64 `json:"cpu"`
	Disks []Disk `json:"disk"`
	//Net []net.IOCountersStat `json:"net"`
	Net []NetInfo `json:"net"`
}

func  String(r Result) string {
	s, err := json.Marshal(r)
	if err!=nil{
		fmt.Printf("Marshal data error:%v\n",err)
	}
	return string(s)
}

func getId()  {
	nets,_ :=net.Interfaces()
	for _, value := range nets {
		if(strings.EqualFold(netCard,value.Name)){
			hardwareAddr := value.HardwareAddr
			fmt.Println("found net card:"+hardwareAddr)
			id = hardwareAddr
			reg := regexp.MustCompile(`(2(5[0-5]{1}|[0-4]\d{1})|[0-1]?\d{1,2})(\.(2(5[0-5]{1}|[0-4]\d{1})|[0-1]?\d{1,2})){3}`)
			for _, addr := range value.Addrs {
				add := addr.Addr
				if(len(reg.FindAllString(add, -1))>0){
					fmt.Println("found ip "+add)
					id = add+" "+id
				}
			}
		}
	}
	/*str := "10.0.0.26"
	matched, err := regexp.MatchString("(2(5[0-5]{1}|[0-4]\\d{1})|[0-1]?\\d{1,2})(\\.(2(5[0-5]{1}|[0-4]\\d{1})|[0-1]?\\d{1,2})){3}", str)
	fmt.Println(matched, err)*/
}

