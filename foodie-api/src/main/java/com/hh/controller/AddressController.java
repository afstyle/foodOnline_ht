package com.hh.controller;

import com.hh.bo.AddressBO;
import com.hh.pojo.UserAddress;
import com.hh.service.AddressService;
import com.hh.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "地址相关", tags = {"用于地址相关的相关接口"})
@RestController
@RequestMapping("address")
public class AddressController {

    @Autowired
    private AddressService addressService;


    @ApiOperation(value = "根据用户id查询收货列表", notes = "根据用户id查询收货列表", httpMethod = "POST")
    @PostMapping("/list")
    public Result list(@RequestParam String userId) {
        if (StringUtils.isBlank(userId)) {
            return Result.errorMsg("");
        }

        List<UserAddress> list = addressService.queryAll(userId);
        return Result.ok(list);
    }

    @ApiOperation(value = "用户新增地址", notes = "用户新增地址", httpMethod = "POST")
    @PostMapping("/add")
    public Result add(@RequestBody AddressBO addressBO) {

        Result checkResult = checkAddress(addressBO);
        if (checkResult.getStatus() != 200) {
            return checkResult;
        }

        addressService.addNewUserAddress(addressBO);
        return Result.ok();
    }

    @ApiOperation(value = "用户更改地址", notes = "用户更改地址", httpMethod = "POST")
    @PostMapping("/update")
    public Result update(@RequestBody AddressBO addressBO) {

        if (StringUtils.isBlank(addressBO.getAddressId())) {
            return Result.errorMsg("");
        }
        Result checkResult = checkAddress(addressBO);
        if (checkResult.getStatus() != 200) {
            return checkResult;
        }

        addressService.updateUserAddress(addressBO);
        return Result.ok();
    }

    @ApiOperation(value = "删除某地址", notes = "删除某地址", httpMethod = "POST")
    @PostMapping("/delete")
    public Result delete(@RequestParam String userId, @RequestParam String addressId) {

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
            return Result.errorMsg("");
        }

        addressService.deleteUserAddress(userId, addressId);
        return Result.ok();
    }


    @ApiOperation(value = "设置为默认地址", notes = "设置为默认地址", httpMethod = "POST")
    @PostMapping("/setDefalut")
    public Result setDefalut(@RequestParam String userId, @RequestParam String addressId) {

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
            return Result.errorMsg("");
        }

        addressService.setDefalut(userId, addressId);
        return Result.ok();
    }






    private Result checkAddress(AddressBO addressBO) {

        // 此处的验证省略...

        return Result.ok();
    }



}
