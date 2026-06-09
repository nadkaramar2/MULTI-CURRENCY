import React, { useState, useEffect } from "react";
import Typography from "@mui/joy/Typography";
import Switch from "@mui/joy/Switch";
import AppLayout from "../../layout/AppLayout";
import CustomAlert from "../../layout/CustomAlert";
import amsApi from "../../api/amsApi";
import { v4 as uuidv4 } from "uuid";
import { XMarkIcon } from "@heroicons/react/24/solid";
import { MagnifyingGlassIcon } from "@heroicons/react/24/outline";
import AutoPagintation from "../../UI/AutoPagintation";
import AutoPagintationReport from "../reports/AutoPagintationReport";
export default function MulticurrencyCharge() {
  const [checked, setChecked] = useState(true);

  const [accounttype, setAccountType] = useState("");
  const splitData = accounttype.split("-");
  let actype = splitData[0].trim();
  const [typedata, setTypedata] = useState([]);
  const [accountnumber, setAccountnumber] = useState("ALL");
  const [tranid, setTranid] = useState("ALL");
  const [chargetype, setChargetype] = useState("");
  const [chargedata, setChargedata] = useState([]);
  const [accuurency, setAccuurency] = useState("");
  const [currencydata, setCurrencydata] = useState([]);
  const [totalElement, setTotalElement] = useState("");

  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");
   const [searchQuery, setSearchQuery] = useState("");
   const [itemPage, setItemPage] = useState(20);
   const [tcount, setTcount] = useState(1);

  const [error, setError] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  const [alldata, setallData] = useState([]);
  let participantID = sessionStorage.getItem("Participantid");
  const handleShowSuccess = (resMessage) => {
    setAlertTitle("Alert");
    setAlertMessage(resMessage);
    setAlertType("blue");
    setShowAlert(true);
  };

  const handleShowError = (resMessage) => {
    setAlertTitle("Error");
    setAlertMessage(resMessage);
    setAlertType("red");
    setShowAlert(true);
  };

  const handleCloseAlert = () => {
    setShowAlert(false);
  };

  useEffect(() => {
    gltype();
  }, []);

  const gltype = async () => {
    try {
      const response = await amsApi.post(
        `accountType/getACCTypeListByParticiptWise`,
        { strParticipantId: participantID },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setTypedata(response.data.accountTypeMasterlistData);
      } else {
        // handleShowError(response.data.message);
      }
    } catch (error) {
      // handleShowError(error.response.data.message);
    }
  };

  useEffect(() => {
    charge();
  }, []);

  const charge = async () => {
    try {
      const response = await amsApi.post(
        `multicurrencychargestypemaster/getchargetypelist`,
        {},
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setChargedata(response.data.multiCurrencyChargesTypeMasters);
      } else {
        // handleShowError(response.data.message);
      }
    } catch (error) {
      // handleShowError(error.response.data.message);
    }
  };

  useEffect(() => {
    currency();
  }, [actype]);

  const currency = async () => {
    try {
      const response = await amsApi.post(
        `currency-master/viewCurrencyList`,
        { accountType: actype },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setCurrencydata(response.data.currencymasterdata);
      } else {
        // handleShowError(response.data.message);
      }
    } catch (error) {
      // handleShowError(error.response.data.message);
    }
  };
  useEffect(() => {
    handleSubmit();
    setError("");
  }, [tcount, itemPage]);
  const handleSubmit = async () => {
    if (
      accounttype === "" ||
      accounttype.length === 0 ||
      accountnumber === "" ||
      chargetype === "" ||
      accuurency === ""
    ) {
      setError(true);
    } else {
      try {
        const response = await amsApi.post(
          `multicurrencycharges/getreport`,

          {
            txnId: tranid,
            chargesType: chargetype,
            accountType: actype,
            accountNumber: accountnumber,
            accountCurrency: accuurency,
            pageSize: itemPage,
            pageNumber: tcount,
          },
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
        if (response.data.code === "S0000") {
          setTotalElement(response.data.totalElementsSize);
          setallData(response.data.multiCurrencyChargesReportsList);
          setChecked(false);
        } else {
          handleShowError(response.data.message);
          setChecked(true);
          setallData([]);
        }
      } catch (error) {
        handleShowError(error.response.data.message);
        setallData([]);
      }
    }
  };

   useEffect(() => {
     if (searchQuery === "" || searchQuery === " ") {
       handleSubmit();
       setError("")
     }
   }, [searchQuery]);

   const Search = async () => {
     try {
       const response = await amsApi.post(
         `multicurrencycharges/chargesReportSearch/pagination/20/1`,
         {
           keyword: searchQuery,
         }
       );
       if (response.data.code === "S0000") {
         setallData(response.data.chargesReportSearch);
         setChecked(false);
         // handleShowSuccess(response.data.message);
       } else {
         handleShowError(response.data.message);
       }
     } catch (error) {
       // handleShowError(error.response.data.message);
     }
   };

  const handleClick = () => {
    setAccountType("");
    setAccountnumber("");
    setChargetype("");
    setAccuurency("");
    setError("");
  };

  // Sorted Tabled data by Date and time wise
  const [sortOrder, setSortOrder] = useState("asc");
  const [sortColumn, setSortColumn] = useState("");
  const handleSort = (column) => {
    if (column === sortColumn) {
      setSortOrder(sortOrder === "asc" ? "desc" : "asc");
    } else {
      setSortColumn(column);
      setSortOrder("asc");
    }
  };

  const sortedData = alldata.slice(0).sort((a, b) => {
    const dateA = new Date(a.strTxnDate);
    const dateB = new Date(b.strTxnDate);
    const timeA = new Date("1970/01/01 " + a.strTxnTime);
    const timeB = new Date("1970/01/01 " + b.strTxnTime);
    if (dateA.getTime() === dateB.getTime()) {
      return sortOrder === "asc" ? timeA - timeB : timeB - timeA;
    } else {
      return sortOrder === "asc" ? dateA - dateB : dateB - dateA;
    }
  });

  const renderSortArrow = (column) => {
    const arrowStyle = {
      cursor: "pointer",
      transition: "transform 0.2s",
    };

    return (
      <span className="cursor-pointer   text-md hover:bg-blue-500  hover:text-white px-1 mx-1">
        {sortColumn === column ? (sortOrder === "asc" ? "▲" : "▼") : "⇅"}
      </span>
    );
  };
  console.log(sortOrder);
  console.log(sortedData);

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-4 bg-blue-200">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Multi Currency Charges Report
              </p>
              <div>
                <button className="inline-flex sm:ml-3 mt-1 sm:mt-0 items-start justify-start px-6 focus:outline-none rounded text-xs font-medium">
                  <Switch
                    checked={checked}
                    onChange={(event) => setChecked(event.target.checked)}
                    slotProps={{
                      track: {
                        children: (
                          <React.Fragment>
                            <Typography
                              component="span"
                              level="inherit"
                              sx={{ ml: "10px" }}
                            >
                              On
                            </Typography>
                            <Typography
                              component="span"
                              level="inherit"
                              sx={{ mr: "8px" }}
                            >
                              Off
                            </Typography>
                          </React.Fragment>
                        ),
                      },
                    }}
                    sx={{
                      "--Switch-thumbSize": "27px",
                      "--Switch-trackWidth": "64px",
                      "--Switch-trackHeight": "31px",
                    }}
                  />
                </button>
              </div>
            </div>
          </div>
        </div>

        {checked && (
          <div className=" md:col-span-2 lg:col-span-1">
            <form className="">
              <div className="overflow-hidden shadow ">
                <div className=" px-4  sm:p-2 bg-white  ">
                  <div className="grid gap-4  md:grid-cols-5 px-4 ">
                    <div>
                      <label
                        htmlFor="text"
                        className="block  text-xs font-semibold text-gray-700 dark:text-white"
                      >
                        Account Type{" "}
                        <span className="text-red-600 px-2">*</span>
                      </label>

                      <select
                        id="accountType"
                        name="accountType"
                        value={accounttype}
                        onChange={(e) => setAccountType(e.target.value)}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                      >
                        <option value="">Select</option>
                        <option value="All">ALL</option>

                        {typedata.map((data) => (
                          <option value={data.strAccountType}>
                            {data.strAccountType}
                          </option>
                        ))}
                      </select>
                      {error && accounttype.length <= 0 ? (
                        <p className="text-red-500   text-xs font-medium">
                          Please Enter Account type
                        </p>
                      ) : (
                        ""
                      )}
                    </div>
                    <div className="">
                      <label
                        htmlFor="text"
                        className="block  text-xs font-semibold text-gray-700 dark:text-white"
                      >
                        Transaction id
                        <span className="text-red-600 px-2">*</span>
                      </label>
                      <input
                        type="text"
                        id="tranid"
                        // disabled={!interestRate}
                        value={tranid}
                        onChange={(e) => {
                          const value = e.target.value;
                          setTranid(value);

                          const regex = /^[0-9]*$/;
                          if (!regex.test(value)) {
                            setErrorMessage(
                              " Only allow numeric digits"
                              // "Please enter only numeric values"
                            );
                          } else {
                            setErrorMessage("");
                          }
                          setError("");
                        }}
                        onBlur={() => {
                          if (tranid.length === 0) {
                            setError("Please Enter Transaction id!");
                            setErrorMessage("");
                          }
                        }}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                        placeholder="Enter Transaction id"
                        autoComplete="off"
                      />
                      {errorMessage ? (
                        <p className="text-red-500 text-sm font-medium">
                          {errorMessage}
                        </p>
                      ) : error && tranid.length <= 0 ? (
                        <p className="text-red-500 text-sm font-medium">
                          Please Enter Transaction id!
                        </p>
                      ) : null}
                    </div>
                    <div className="">
                      <label
                        htmlFor="text"
                        className="block  text-xs font-semibold text-gray-700 dark:text-white"
                      >
                        Account number
                        <span className="text-red-600 px-2">*</span>
                      </label>
                      <input
                        type="text"
                        id="Grace Period In Days"
                        // disabled={!interestRate}
                        value={accountnumber}
                        onChange={(e) => {
                          const value = e.target.value;
                          setAccountnumber(value);

                          const regex = /^[0-9]*$/;
                          if (!regex.test(value)) {
                            setErrorMessage(
                              " Only allow numeric digits"
                              // "Please enter only numeric values"
                            );
                          } else {
                            setErrorMessage("");
                          }
                          setError("");
                        }}
                        onBlur={() => {
                          if (accountnumber.length === 0) {
                            setError("Please Enter Account number!");
                            setErrorMessage("");
                          }
                        }}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                        placeholder="Enter Account Number"
                        autoComplete="off"
                      />
                      {errorMessage ? (
                        <p className="text-red-500 text-sm font-medium">
                          {errorMessage}
                        </p>
                      ) : error && accountnumber.length <= 0 ? (
                        <p className="text-red-500 text-sm font-medium">
                          Please Enter Account number!
                        </p>
                      ) : null}
                    </div>
                    <div>
                      <label
                        htmlFor="text"
                        className="block  text-xs font-semibold text-gray-700 dark:text-white"
                      >
                        Charge Type <span className="text-red-600 px-2">*</span>
                      </label>

                      <select
                        id="chargetype"
                        name="chargetype"
                        value={chargetype}
                        onChange={(e) => setChargetype(e.target.value)}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                      >
                        <option value="">Select</option>
                        <option value="ALL">ALL</option>
                        {chargedata.map((data) => (
                          <option value={data.chargeType}>
                            {data.chargeType}
                          </option>
                        ))}
                      </select>
                      {error && chargetype.length <= 0 ? (
                        <p className="text-red-500   text-xs font-medium">
                          Please Enter Charge Type
                        </p>
                      ) : (
                        ""
                      )}
                    </div>
                    <div>
                      <label
                        htmlFor="text"
                        className="block  text-xs font-semibold text-gray-700 dark:text-white"
                      >
                        Account Currency Type{" "}
                        <span className="text-red-600 px-2">*</span>
                      </label>

                      <select
                        id="accuurency"
                        name="accuurency"
                        value={accuurency}
                        onChange={(e) => setAccuurency(e.target.value)}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                      >
                        <option value="">Select</option>
                        <option value="ALL">ALL</option>

                        {currencydata.map((data) => (
                          <option value={data.currencyCode}>
                            {data.currencyCode}
                          </option>
                        ))}
                      </select>
                      {error && accuurency.length <= 0 ? (
                        <p className="text-red-500   text-xs font-medium">
                          Please Select Account Currency Type
                        </p>
                      ) : (
                        ""
                      )}
                    </div>
                    <div className="">
                      <button
                        title="Click Search Button"
                        type="button"
                        onClick={handleSubmit}
                        data-modal-toggle="defaultModal"
                        className="inline-flex justify-center  bg-blue-500 hover:bg-blue-700 b  focus:ring-2 focus:ring-blue-800 focus:ring-offset-2 border-blue-700 py-1 px-5 mx-2 my-3  text-xs font-bold text-white rounded "
                      >
                        <MagnifyingGlassIcon className="h-4 w-3" />
                      </button>

                      <button
                        title="Clear Data"
                        type="button"
                        onClick={handleClick}
                        className="inline-flex justify-center  bg-rose-500 hover:bg-rose-700 b  focus:ring-2 focus:ring-rose-800 focus:ring-offset-2 border-rose-700 py-1 px-5 mx-2 my-5  text-xs font-bold text-white rounded "
                      >
                        <XMarkIcon className="h-4 w-3" />
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </form>
          </div>
        )}

        <AutoPagintationReport
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          searchData={setSearchQuery}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={alldata[0]?.strTotalCount}
          reCallApi={Search}
        />
        {/* <AutoPagintation
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={totalElement}
          reCallApi={handleSubmit}
        /> */}

        <div className="overflow-x-auto relative shadow-md ">
          <>
            <div className="table-wrp block max-h-[27rem] max-w-[27rem]">
              <table className="w-full text-xs text-left text-black dark:text-blue-100">
                <thead className="text-xm border-b sticky top-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
                  <th
                    scope="col"
                    className="py-2.5 px-6 whitespace-nowrap"
                    onClick={() => handleSort("strTxnDate")}
                  >
                    txn Date {renderSortArrow("strTxnDate")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-6 whitespace-nowrap"
                    onClick={() => handleSort("strTxnTime")}
                  >
                    TXN time {renderSortArrow("strTxnTime")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-6 whitespace-nowrap"
                    onClick={() => handleSort("txnId")}
                  >
                    txn Id {renderSortArrow("txnId")}
                  </th>

                  <th
                    scope="col"
                    className="py-2.5 px-6 whitespace-nowrap"
                    onClick={() => handleSort("txnType")}
                  >
                    txn Type {renderSortArrow("txnType")}
                  </th>

                  <th
                    scope="col"
                    className="py-2.5 px-6 whitespace-nowrap"
                    onClick={() => handleSort("accountType")}
                  >
                    account Type {renderSortArrow("accountType")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-6 whitespace-nowrap"
                    onClick={() => handleSort("accountNumber")}
                  >
                    account Number {renderSortArrow("accountNumber")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-6 whitespace-nowrap"
                    onClick={() => handleSort("chargesType")}
                  >
                    charges Type {renderSortArrow("chargesType")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-6 whitespace-nowrap"
                    onClick={() => handleSort("accountCurrency")}
                  >
                    account Currency {renderSortArrow("accountCurrency")}
                  </th>

                  <th
                    scope="col"
                    className="py-2.5 px-6 whitespace-nowrap"
                    onClick={() => handleSort("glAccountType")}
                  >
                    GL Account Type {renderSortArrow("glAccountType")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-6 whitespace-nowrap"
                    onClick={() => handleSort("glAccountNumber")}
                  >
                    gl Account Number {renderSortArrow("glAccountNumber")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-6 whitespace-nowrap"
                    onClick={() => handleSort("baseCurrency")}
                  >
                    base Currency {renderSortArrow("baseCurrency")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-6 whitespace-nowrap"
                    onClick={() => handleSort("txnAmt")}
                  >
                    txn Amt {renderSortArrow("txnAmt")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-6 whitespace-nowrap"
                    onClick={() => handleSort("baseCurrencyAmt")}
                  >
                    base Currency Amt {renderSortArrow("baseCurrencyAmt")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-6 whitespace-nowrap"
                    onClick={() => handleSort("baseConversionRate")}
                  >
                    base Conversion Rate {renderSortArrow("baseConversionRate")}
                  </th>
                </thead>

                <tbody>
                  {sortedData.length > 0 ? (
                    <>
                      {sortedData
                        
                        .map((data) => (
                          <tr
                            key={uuidv4()}
                            className=" border-b dark:border-neutral-500"
                          >
                            <td className="px-6 py-2.5 whitespace-nowrap">
                              <div className="text-xs font-medium text-gray-900">
                                {data.strTxnDate}
                              </div>
                            </td>
                            <td className="px-6 py-2.5 whitespace-nowrap">
                              <div className="text-xs font-medium text-gray-900">
                                {data.strTxnTime}
                              </div>
                            </td>
                            <td className="px-6 py-2.5 whitespace-nowrap">
                              <div className="text-xs font-medium text-gray-900">
                                {data.txnId || "-"}
                              </div>
                            </td>

                            <td className="px-6 py-2.5 whitespace-nowrap">
                              <div className="text-xs font-medium text-gray-900">
                                {data.txnType || "-"}
                              </div>
                            </td>

                            <td className="px-6 py-2.5 whitespace-nowrap">
                              <div className="text-xs font-medium text-gray-900">
                                {data.accountType}
                              </div>
                            </td>
                            <td className="px-6 py-2.5 whitespace-nowrap">
                              <div className="text-xs font-medium text-gray-900">
                                {data.accountNumber || "-"}
                              </div>
                            </td>
                            <td className="px-6 py-2.5 whitespace-nowrap">
                              <div className="text-xs font-medium text-gray-900">
                                {data.chargesType || "-"}
                              </div>
                            </td>
                            <td className="px-6 py-2.5 whitespace-nowrap">
                              <div className="text-xs font-medium text-gray-900">
                                {data.accountCurrency || "-"}
                              </div>
                            </td>
                            <td className="px-6 py-2.5 whitespace-nowrap">
                              <div className="text-xs font-medium text-gray-900">
                                {""}
                                {data.glAccountType || "-"}
                              </div>
                            </td>
                            <td className="px-6 py-2.5 whitespace-nowrap">
                              <div className="text-xs font-medium text-gray-900">
                                {""}
                                {data.glAccountNumber || "-"}
                              </div>
                            </td>
                            <td className="px-6 py-2.5 whitespace-nowrap">
                              <div className="text-xs font-medium text-gray-900">
                                {""}
                                {data.baseCurrency || "-"}
                              </div>
                            </td>
                            <td className="px-6 py-2.5 whitespace-nowrap text-end">
                              <div className="text-xs font-medium text-gray-900">
                                {""}
                                {data.txnAmt || "-"}
                              </div>
                            </td>
                            <td className="px-6 py-2.5 whitespace-nowrap text-end">
                              <div className="text-xs font-medium text-gray-900">
                                {""}
                                {data.baseCurrencyAmt || "-"}
                              </div>
                            </td>
                            <td className="px-6 py-2.5 whitespace-nowrap text-end">
                              <div className="text-xs font-medium text-gray-900">
                                {""}
                                {data.baseConversionRate || "-"}
                              </div>
                            </td>
                          </tr>
                        ))}
                    </>
                  ) : (
                    <tr>
                      <td colSpan="15" className="px-6 py-1 text-center">
                        <span className="text-sm font-normal text-gray-500">
                          No data available.
                        </span>
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </>
        </div>
      </div>

      {showAlert && (
        <CustomAlert
          title={alertTitle}
          message={alertMessage}
          type={alertType}
          onClose={handleCloseAlert}
        />
      )}
    </AppLayout>
  );
}
