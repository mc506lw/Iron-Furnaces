<p align="center">
  <img src="./readme-header.png" alt="Iron Furnaces Banner" width="100%">
</p>

<p align="center">
  <h1>🔥 Iron Furnaces</h1>
  <strong>高级熔炉系统 - 9种等级 · 升级组件 · 美观GUI</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Minecraft-26.1.2-brightgreen?style=flat-square" alt="Minecraft">
  <img src="https://img.shields.io/badge/Rebar-依赖-orange?style=flat-square" alt="Rebar">
  <a href="#快速开始"><kbd>🚀 快速开始</kbd></a>
  <a href="#熔炉等级"><kbd>🔥 等级</kbd></a>
  <a href="#升级系统"><kbd>⬆️ 升级</kbd></a>
</p>

---

## 📋 简介

**Iron Furnaces** 是 **Rebar 框架的附属插件**，复刻经典模组 **[Iron Furnaces](https://modrinth.com/mod/iron-furnaces)**。

本插件为你的服务器添加**多级高级熔炉**，从铜到下界合金，9种不同速度的熔炉满足你所有需求！

### ⚠️ 前置要求

| 要求 | 版本 | 说明 |
|------|------|------|
| **Minecraft** | **26.1.2** | 仅支持此版本（Paper 或其分支）|
| **Rebar** | **0.39.1+** | 必须安装，本插件是 Rebar 的附属插件 |

> ⚠️ **重要：** 本插件**必须配合 Rebar 使用**，无法独立运行！请先安装 [Rebar](https://github.com/pylonmc/Rebar) 插件。

### ✨ 核心特性

- 🔥 **9种熔炉等级** - 最快可达原版 **36倍** 速度
- ⬆️ **6种升级组件** - 自定义熔炉能力（加速、省燃料、自动化等）
- 🎨 **精美界面** - 基于 InvUI 的简洁操作界面
- 🌈 **特殊熔炉** - 彩虹熔炉（颜色变化+批量处理）、水晶熔炉（半透明）
- 🌐 **中文支持** - 完整的简体中文界面
- 🔧 **物流集成** - 支持 Rebar 自动化输入输出

---

## 🚀 快速开始

### 安装步骤（30秒）

1. **安装 Rebar** - 确保 `plugins` 文件夹已有 Rebar 插件
2. **下载本插件** - 获取 `Iron-Furnaces-1.0.0.jar`
3. **放入服务器** - 将 jar 文件放入 `plugins` 文件夹
4. **重启服务器** - 输入 `/reload` 或重启服务器

完成！🎉

### 获取熔炉

```minecraft
/rb give @p ironfurnaces:iron_furnace      # 铁熔炉
/rb give @p ironfurnaces:diamond_furnace   # 钻石熔炉
/rb give @p ironfurnaces:netherite_furnace # 下界合金熔炉
/rb give @p ironfurnaces:rainbow_furnace   # 彩虹熔炉
# ... 更多见下方"熔炉等级"
```

---

## 🎮 使用方法

1. **放置熔炉** - 像普通方块一样放置
2. **右键打开** - 点击熔炉打开操作界面
3. **放入燃料** - 底部槽位放燃料（煤炭、木炭等）
4. **放入物品** - 左侧槽位放要烧炼的东西
5. **取出成品** - 右侧槽位自动输出结果
6. **点击⚒️按钮** - 打开升级界面（可选）

就这么简单！

---

## 🔥 熔炉等级

复刻原版 Iron Furnaces MOD 的经典等级体系：

| 等级 | 材质 | 速度 | 说明 |
|------|------|------|------|
| 🟤 **铜熔炉** | Copper Block | 180t/item | 入门级，适合初期 |
| ⬜ **铁熔炉** | Iron Block | 160t/item | 进阶选择 |
| 🟨 **金熔炉** | Gold Block | 120t/item | 性价比之选 |
| 💎 **钻石熔炉** | Diamond Block | 80t/item | 推荐日常使用 |
| 💚 **绿宝石熔炉** | Emerald Block | 40t/item | 大规模生产 |
| 💠 **水晶熔炉** | Stained Glass | 40t/item | 半透明外观 |
| ⬛ **黑曜石熔炉** | Obsidian | 20t/item | 工业级效率 |
| 🟫 **下界合金熔炉** | Netherite Block | 5t/item | 极速熔炼（36倍！）|
| 🌈 **彩虹熔炉** | Wool (动态) | 20t/item | 特殊效果 + 批量处理 |

---

## ⬆️ 升级系统

暂未完成

---

## 🌈 特殊熔炉

### 彩虹熔炉 🌈

致敬原版 MOD 的终极熔炉，拥有独特能力：

- ✨ **颜色循环** - 每10tick自动变换16种羊毛颜色
- 🎆 **彩色粒子** - 烧炼时释放彩色烟雾粒子特效
- 📦 **批量处理** - 燃料充足时可一次烧炼多个物品

**适合场景：** 高效批量生产

### 水晶熔炉 💠

- 🔮 **半透明外观** - 使用玻璃材质，光线可穿透
- 📐 **边框装饰** - 自动生成12条玻璃边框线条
- 💎 **独特视觉效果** - 区别于普通熔炉的视觉体验

---

## 🛠️ 命令与权限

| 命令 | 说明 | 权限节点 |
|------|------|---------|
| `/rb give @s ironfurnaces:xxx_furnace` | 获取指定熔炉 | `ironfurnaces.give.*` |
| `/ironfurnaces reload` | 重载插件配置 | `ironfurnaces.admin` |

*详细权限配置请查看插件生成的 `config.yml`*

---

## 📞 支持与反馈

- **问题报告**: [GitHub Issues](../../issues)
- **功能建议**: [GitHub Discussions](../../discussions)
- **原版 MOD**: [Iron Furnaces (Modrinth)](https://modrinth.com/mod/iron-furnaces)

---

<div align="center">

**享受更快的熔炼体验！** 🔥

<p>
 Made with ❤️ for <strong>Minecraft 26.1.2</strong> · Powered by <strong>mc506lw</strong>
</p>

<p>
<em>复刻经典 · 致敬 Iron Furnaces MOD</em>
</p>

</div>
