import React, { useState, useEffect, useRef, Fragment } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import swal from "sweetalert";
import { utils, write } from "xlsx";
import { useReactToPrint } from "react-to-print";
import Switch from "@mui/joy/Switch";
import ReactLoading from "react-loading";
import Typography from "@mui/joy/Typography";
import { Dialog, Transition } from "@headlessui/react";
import { XMarkIcon } from "@heroicons/react/24/solid";
import AutoPagintation from "../../UI/AutoPagintation";
import {
  ArrowDownTrayIcon,
  MagnifyingGlassIcon,
} from "@heroicons/react/24/outline";
import CustomAlert from "../../layout/CustomAlert";
import AutoPagintationReport from "../reports/AutoPagintationReport";
const ViewCurrencyTransferMaster = () => {
  let participantID = sessionStorage.getItem("Participantid");
  const [alldata, setalldata] = useState([]);
  const [type, setType] = useState([]);
  const [code, setcode] = useState([]);
  const [isLoadig, setisLoadig] = useState(false);
  const [startdate, setStartdate] = useState("");
  const [enddate, setEnddata] = useState("");
 
  const [error, setError] = useState("");
   const [searchQuery, setSearchQuery] = useState("");
   const [itemPage, setItemPage] = useState(20);
   const [tcount, setTcount] = useState(1);
  const [totalElement, setTotalElement] = useState("");
  const [accounttype, setAccountType] = useState("");
  const splitData = accounttype.split("-");
  let actype = splitData[0].trim();
  // Function to handle changes in the search input field
  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");
  const [baseaccountnumber, setbaseaccountnumber] = useState("All");
  const [sweepincurrencycode, setsweepincurrencycode] = useState("");
  const [sweepoutcurrencycode, setsweepoutcurrencycode] = useState("");
  const [tranid, setTranid] = useState("ALL");
  //  const [error, setError] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
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
  const handleClick = () => {
    setStartdate("");
    setEnddata("");
    setDuration("");
  };
  const [checked, setChecked] = useState(true);
  const [dateOpen, setDateOpen] = useState(false);
  const [duration, setDuration] = useState("");
  const yesterday = new Date();
  function remainDays(leftDate) {
    yesterday.setDate(yesterday.getDate() - leftDate);
  }
  function padTo2Digits(num) {
    return num.toString().padStart(2, "0");
  }
  function formatDate(date) {
    return [
      date.getFullYear(),
      padTo2Digits(date.getMonth() + 1),
      padTo2Digits(date.getDate()),
    ].join("-");
  }
  const current = new Date();
  const date = `${current.getFullYear()}-${String(
    current.getMonth() + 1
  ).padStart(2, "0")}-${String(current.getDate()).padStart(2, "0")}`;
  const daysInCurrentMonth = `${String(current.getDate()).padStart(2, "0")}`;
  function endOfMonth(date) {
    return new Date(date.getFullYear(), date.getMonth() + 0, 0);
  }
  function startOfMonth(date) {
    return new Date(date.getFullYear(), date.getMonth() - 1, 1);
  }
  const DurationHandler = (e) => {
    setDateOpen(false);
    const selectData = e.target.value;
    setDuration(selectData);
    if (selectData === "0") {
      setStartdate(date);
      setEnddata(date);
    } else if (selectData === "1") {
      remainDays(1);
      setStartdate(formatDate(yesterday));
      setEnddata(formatDate(yesterday));
    } else if (selectData === "7") {
      remainDays(7);
      setStartdate(formatDate(yesterday));
      setEnddata(date);
    } else if (selectData === "10") {
      remainDays(10);
      setStartdate(formatDate(yesterday));
      setEnddata(date);
    } else if (selectData === "custom") {
      setDateOpen(true);
    } else if (selectData === "cMonth") {
      remainDays(parseInt(daysInCurrentMonth) - 1);
      setStartdate(formatDate(yesterday));
      setEnddata(date);
    } else if (selectData === "pMonth") {
      setStartdate(formatDate(startOfMonth(current)));
      setEnddata(formatDate(endOfMonth(current)));
    }
  };

  useEffect(() => {
    handleSubmit();
  }, [tcount, itemPage]);

  const handleSubmit = async () => {
    if (
      baseaccountnumber === "" ||
      actype === "" ||
      sweepincurrencycode === ""
    ) {
      setError(true);
      //  swal("please enter a valid Input (non-empty values).");
    } else if (startdate > enddate) {
      swal("please choose lesser date from To Date");
    } else {
      setisLoadig(true);
      try {
        const response = await amsApi.post(
          `currencyTransfer/viewCurrencyTransferMaster`,
          {
            tranId: tranid,
            baseaccounttype: actype,
            baseaccountnumber: baseaccountnumber,
            sweepincurrencycode: sweepincurrencycode,
            sweepoutcurrencycode: sweepoutcurrencycode,
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
          setalldata(response.data.currenymasterData);
        
          setChecked(false);
          setisLoadig(false);
        } else {
          handleShowError(response.data.message);
          setChecked(true);
          setalldata([]);
          setisLoadig(false);
        }
      } catch (error) {
        handleShowError(
          "Sorry, the server is under mentaines. Please try again later"
        );
        setalldata([]);
        setisLoadig(false);
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
         `currencyTransfer/currencyTransferMasterSearch/pagination/20/1`,
         {
           keyword: searchQuery,
         }
       );
       if (response.data.code === "S0000") {
         setalldata(response.data.currencyTranMasterSearch);
         setChecked(false);
         // handleShowSuccess(response.data.message);
       } else {
         handleShowError(response.data.message);
       }
     } catch (error) {
       // handleShowError(error.response.data.message);
     }
   };

  // Dropdown Account Type

  useEffect(() => {
    Accounttype();
  }, []);

  const Accounttype = async () => {
    try {
      const response = await amsApi.post(
        `accountType/getACCTypeListByParticiptWise`,

        {
          strParticipantId: participantID,
        },

        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (response.data.code === "S0000") {
        setType(response.data.accountTypeMasterlistData);
      } else {
        // showError(response.data.message);
      }
    } catch (error) {
      // showError(error.response.data.message);
    }
  };
  // Dropdown Account Type

  useEffect(() => {
    CurreCode();
  }, []);

  const CurreCode = async () => {
    try {
      const response = await amsApi.post(
        `currencyTransfer/getCurrencyCode`,

        {},

        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (response.data.code === "S0000") {
        setcode(response.data.currencymasterdata);
      } else {
        // showError(response.data.message);
      }
    } catch (error) {
      // showError(error.response.data.message);
    }
  };

  // Download pdf and Excel
  const conponentPDF = useRef();
  const generatePDF = useReactToPrint({
    content: () => conponentPDF.current,
    documentTitle: "TableData",
  });
  const generateExcel = (data) => {
    const worksheet = utils.json_to_sheet(data);
    const workbook = { Sheets: { data: worksheet }, SheetNames: ["data"] };
    const excelBuffer = write(workbook, { bookType: "xlsx", type: "array" });
    const blob = new Blob([excelBuffer], {
      type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
    });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = "Transaction Report.xlsx";
    a.click();
  };

  const [isOpen, setIsOpen] = useState(false);
  // Function to open the modal
  const openModal = () => {
    setIsOpen(true);
  };
  // Function to close the modal
  const closeModal = () => {
    setIsOpen(false);
  };
  const [sortColumn, setSortColumn] = useState(""); // State to track the sorted column
  const [sortOrder, setSortOrder] = useState("asc"); // State to track sorting order

  const handleSort = (column) => {
    if (column === sortColumn) {
      // If clicking on the same column, toggle the sorting order
      setSortOrder(sortOrder === "asc" ? "desc" : "asc");
    } else {
      // If clicking on a different column, set the new column and default to ascending order
      setSortColumn(column);
      setSortOrder("asc");
    }
  };

  // Sort the data based on the chosen column and order
  const sortedData = [...alldata].sort((a, b) => {
    if (sortOrder === "asc") {
      const aValue = a[sortColumn] || "";
      const bValue = b[sortColumn] || "";
      return aValue.localeCompare(bValue);
    } else {
      const aValue = a[sortColumn] || "";
      const bValue = b[sortColumn] || "";
      return bValue.localeCompare(aValue);
    }
  });
  const AccountTypeHandle = (e) => {
    const acc = e.target.value;
    if (acc === "All") {
      setsweepincurrencycode("All");
      setsweepoutcurrencycode("All");
    }
    setAccountType(acc);
  };

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
  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2">
          <div className="px-2 sm:px-2 bg-blue-200 ">
            <div className=" flex  items-center justify-between">
              <p className="flex justify-start font-sans sm:text-xm text-black text-sm leading-normal">
                View Currency Transfer Master
              </p>

              {/* </p> */}
              {isLoadig && (
                <div className="flex items-center justify-center my-10">
                  <ReactLoading
                    type="balls"
                    color="#0143E2"
                    height={"10%"}
                    width={"8%"}
                  />
                </div>
              )}
              <div>
                <button className="inline-flex sm:ml-3  sm:mt-0 items-start justify-start px-2  focus:outline-none rounded text-xs font-medium">
                 
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
          <div className=" md:col-span-2 lg:col-span-1  ">
            <div className="overflow-hidden shadow ">
              <div className=" px-2  sm:p-2 bg-white  ">
                <div className="grid gap-4  md:grid-cols-5  px-3">
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700 dark:text-white"
                    >
                      Base Account Type
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <select
                      id="accountType"
                      name="accountType"
                      value={accounttype}
                      onChange={(e) => AccountTypeHandle(e)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                    >
                      <option value="">Select</option>
                      <option value="All">ALL</option>
                      {type.map((data) => (
                        <option value={data.strAccountType}>
                          {data.strAccountType}-{data.strDescription}
                        </option>
                      ))}
                    </select>
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

                  <div>
                    <label
                      htmlFor="duration"
                      className="block  text-xs font-semibold text-gray-700 dark:text-white"
                    >
                      Account No
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="baseaccountnumber"
                      value={baseaccountnumber}
                      onChange={(e) => setbaseaccountnumber(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Account No"
                    />
                  </div>
                  {accounttype !== "All" && (
                    <>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700 dark:text-white"
                        >
                          Sweep In
                          <span className="text-red-600 px-2">*</span>
                        </label>
                        <select
                          id="sweepincurrencycode"
                          name="sweepincurrencycode"
                          value={sweepincurrencycode}
                          onChange={(e) =>
                            setsweepincurrencycode(e.target.value)
                          }
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                        >
                          <option value="">Select</option>
                          <option value="All">ALL</option>

                          {/* {code.map((data) => (
                            <option value={data.currencyCode}>
                              {data.currencyCode}
                            </option>
                          ))} */}
                        </select>
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700 dark:text-white"
                        >
                          Sweep Out
                          <span className="text-red-600 px-2">*</span>
                        </label>
                        <select
                          id="sweepoutcurrencycode"
                          name="sweepoutcurrencycode"
                          value={sweepoutcurrencycode}
                          onChange={(e) =>
                            setsweepoutcurrencycode(e.target.value)
                          }
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                        >
                          <option value="">Select</option>
                          <option value="All">ALL</option>
                          {/* {code.map((data) => (
                            <option value={data.currencyCode}>
                              {data.currencyCode}
                            </option>
                          ))} */}
                        </select>
                      </div>
                    </>
                  )}
                  <div className=" mt-1">
                    <button
                      title="Click Search Button"
                      type="submit"
                      onClick={handleSubmit}
                      data-modal-toggle="defaultModal"
                      className="bg-blue-500 hover:bg-blue-700 focus:ring-2 focus:ring-blue-800   focus:ring-offset-2 text-white font-md py-2 px-4 border border-blue-700 rounded  shadow-lg inner"
                    >
                      <MagnifyingGlassIcon className="h-2.5 w-3" />
                    </button>
                    <button
                      title="Clear Data"
                      type="button"
                      onClick={handleClick}
                      className="inline-flex justify-center bg-rose-500 hover:bg-rose-700 focus:ring-2 focus:ring-rose-800 focus:ring-offset-2 py-2 px-4 mx-2 my-3 font-md  text-white  border  shadow-lg inner border-rose-700 rounded"
                    >
                      <XMarkIcon className="h-2.5 w-3" />
                    </button>
                    <button
                      title="Go Back"
                      onClick={() => openModal()}
                      className="inline-flex justify-center bg-green-500 hover:bg-green-700 focus:ring-2 focus:ring-green-800 focus:ring-offset-2 py-2 px-4 mx-2 my-3 font-md  text-white  border  shadow-lg inner border-green-700 rounded"
                    >
                      <ArrowDownTrayIcon className="h-2.5 w-3" />
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        )}
        {/* table */}
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

        <div className="overflow-x-auto relative shadow-md mt-1 ">
          <div className="table-wrp block max-h-[33rem] max-w-[27rem]">
            <div ref={conponentPDF} style={{ width: "100%" }}>
              <table className="w-full text-xs  text-black dark:text-blue-100">
                <thead className="text-xm border-b sticky top-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
                  <th
                    scope="col"
                    className="py-3 px-2 whitespace-nowrap"
                    onClick={() => handleSort("tranId")}
                  >
                    tran Id {renderSortArrow("tranId")}
                  </th>
                  <th
                    scope="col"
                    className="py-3 px-2 whitespace-nowrap"
                    onClick={() => handleSort("baseaccounttype")}
                  >
                    Base Account Type {renderSortArrow("baseaccounttype")}
                  </th>
                  <th
                    scope="col"
                    className="py-3 px-2 whitespace-nowrap"
                    onClick={() => handleSort("baseaccountnumber")}
                  >
                    Base Account No{renderSortArrow("baseaccountnumber")}
                  </th>
                  <th
                    scope="col"
                    className="py-3 px-2 whitespace-nowrap"
                    onClick={() => handleSort("sweepoutcurrencycode")}
                  >
                    sweep out currency code{" "}
                    {renderSortArrow("sweepoutcurrencycode")}
                  </th>
                  <th
                    scope="col"
                    className="py-3 px-2 whitespace-nowrap"
                    onClick={() => handleSort("sweepoutcurrencyvalue")}
                  >
                    {""}
                    sweep out currency value{" "}
                    {renderSortArrow("sweepoutcurrencyvalue")}
                  </th>
                  <th
                    scope="col"
                    className="py-3 px-2 whitespace-nowrap "
                    onClick={() => handleSort("sweepoutamnt")}
                  >
                    Sweep Out Amount {renderSortArrow("sweepoutamnt")}
                  </th>
                  <th
                    scope="col"
                    className="py-3 px-2 whitespace-nowrap "
                    onClick={() => handleSort("sweepinamnt")}
                  >
                    Sweep In Amount {renderSortArrow("sweepinamnt")}
                  </th>

                  <th
                    scope="col"
                    className="py-3 px-2 whitespace-nowrap"
                    onClick={() => handleSort("sweepincurrencycode")}
                  >
                    Sweep In Currency Code{" "}
                    {renderSortArrow("sweepincurrencycode")}
                  </th>

                  <th
                    scope="col"
                    className="py-3 px-2 whitespace-nowrap"
                    onClick={() => handleSort("sweepincurrencyvalue")}
                  >
                    Sweep In Currency Value{" "}
                    {renderSortArrow("sweepincurrencyvalue")}
                  </th>

                  <th
                    scope="col"
                    className="py-3 px-2 whitespace-nowrap"
                    onClick={() => handleSort("createdby")}
                  >
                    Created By{renderSortArrow("createdby")}
                  </th>
                </thead>
                <tbody>
                  {sortedData.length > 0 ? (
                    <>
                      {sortedData
                        .map(
                          ({
                            tranId,
                            sweepoutcurrencycode,
                            sweepoutcurrencyvalue,
                            sweepoutamnt,
                            sweepinamnt,
                            sweepincurrencycode,
                            sweepincurrencyvalue,
                            baseaccounttype,
                            baseaccountnumber,
                            createdby,
                          }) => (
                            <tr
                              // key={uuidv4()}
                              className="border-b dark:border-neutral-500"
                            >
                              <td className="whitespace-nowrap px-2 py-3">
                                <span className="text-xs font-medium text-gray-900">
                                  {tranId || "-"}
                                </span>
                              </td>
                              <td className="whitespace-nowrap px-2 py-3">
                                <span className="text-xs font-medium text-gray-900">
                                  {baseaccounttype || "-"}
                                </span>
                              </td>
                              <td className="whitespace-nowrap px-2 py-3">
                                <span className="text-xs font-medium text-gray-900">
                                  {baseaccountnumber || "-"}
                                </span>
                              </td>
                              <td className="whitespace-nowrap px-2 py-3 ">
                                <span className="text-xs font-medium text-gray-900">
                                  {sweepoutcurrencycode || "-"}
                                </span>
                              </td>
                              <td className="whitespace-nowrap px-2 py-3 text-end ">
                                <span className="text-xs font-medium  text-gray-900">
                                  {sweepoutcurrencyvalue || "-"}
                                </span>
                              </td>
                              <td className="whitespace-nowrap px-2 py-3  text-end">
                                <span className="text-xs font-medium text-gray-900">
                                  {sweepoutamnt || "-"}
                                </span>
                              </td>
                              <td className="whitespace-nowrap  text-end px-2 py-3">
                                <span className="text-xs font-medium text-gray-900">
                                  {sweepinamnt || "-"}
                                </span>
                              </td>
                              <td className="whitespace-nowrap px-2 py-3 text-center">
                                <span className="text-xs font-medium text-gray-900">
                                  {sweepincurrencycode || "-"}
                                </span>
                              </td>

                              <td className="whitespace-nowrap px-2 py-3 text-right">
                                <span className="text-xs font-medium text-gray-900">
                                  {sweepincurrencyvalue || "-"}
                                </span>
                              </td>

                              <td className="whitespace-nowrap px-2 py-3">
                                <span className="text-xs font-medium text-gray-900">
                                  {createdby || "-"}
                                </span>
                              </td>
                            </tr>
                          )
                        )}
                    </>
                  ) : (
                    <tr>
                      <td colSpan="10" className="px-2 py-1.5 text-center">
                        <span className="text-sm font-normal text-gray-500">
                          No data available.
                        </span>
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
      {/* <MainPagination itemPageData={setItemPage} tCountData={setTcount} /> */}
      {/* </div> */}
      {/* Your existing code here */}

      {showAlert && (
        <CustomAlert
          title={alertTitle}
          message={alertMessage}
          type={alertType}
          onClose={handleCloseAlert}
        />
      )}
      {/* Delete model */}
      <Transition appear show={isOpen} as={Fragment}>
        <Dialog as="div" className="relative z-10" onClose={() => {}}>
          <Transition.Child
            as={Fragment}
            enter="ease-out duration-300"
            enterFrom="opacity-0"
            enterTo="opacity-100"
            leave="ease-in duration-200"
            leaveFrom="opacity-100"
            leaveTo="opacity-0"
          >
            <div className="fixed inset-0 bg-black bg-opacity-25" />
          </Transition.Child>

          <div className="fixed inset-0 overflow-y-auto">
            <div className="flex min-h-full items-center justify-center p-4 text-center">
              <Transition.Child
                as={Fragment}
                enter="ease-out duration-300"
                enterFrom="opacity-0 scale-95"
                enterTo="opacity-100 scale-100"
                leave="ease-in duration-200"
                leaveFrom="opacity-100 scale-100"
                leaveTo="opacity-0 scale-95"
              >
                <Dialog.Panel className="w-full max-w-md transform overflow-hidden rounded-2xl bg-white p-6 text-left align-middle shadow-xl transition-all">
                  <Dialog.Title
                    as="h3"
                    className="text-lg font-medium leading-6 text-gray-900"
                  >
                    Download
                  </Dialog.Title>
                  <div className="mt-2">
                    <p className="text-xs text-gray-500">
                      Are you sure you want to download file's ?
                    </p>
                  </div>
                  <div className=" px-2 py-1 sm:px-2 sm:flex sm:flex-row-reverse">
                    <button
                      type="button"
                      className="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-2 py-2 bg-green-600 text-base font-medium text-white hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 sm:ml-3 sm:w-auto sm:text-xs"
                      onClick={() => generatePDF(alldata)}
                    >
                      PDF
                    </button>
                    <button
                      type="button"
                      className="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-2 py-2 bg-orange-600 text-base font-medium text-white hover:bg-orange-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 sm:ml-3 sm:w-auto sm:text-xs"
                      onClick={() => generateExcel(alldata)}
                    >
                      Excel
                    </button>
                    <button
                      type="button"
                      className="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-2 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 sm:mt-0 sm:ml-3 sm:w-auto sm:text-xs"
                      onClick={closeModal}
                    >
                      Cancel
                    </button>
                  </div>
                </Dialog.Panel>
              </Transition.Child>
            </div>
          </div>
        </Dialog>
      </Transition>

      {/*search Transaction  */}

      {/* Dilog box */}
    </AppLayout>
  );
};

export default ViewCurrencyTransferMaster;
